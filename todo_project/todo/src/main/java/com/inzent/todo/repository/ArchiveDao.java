package com.inzent.todo.repository;

import java.util.List;

import com.inzent.todo.dto.ArchiveSubDto;
import com.inzent.todo.dto.ArchiveSuperDto;
import com.inzent.todo.vo.RestoreSubVo;
import com.inzent.todo.vo.RestoreSuperVo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ArchiveDao {

    @Autowired
    private SqlSession sqlsession;

    public List<ArchiveSuperDto> getArchiveSupers(String userId) {

        return sqlsession.selectList("archive.getArchiveSupers", userId);
    }

    public List<ArchiveSubDto> getArchiveSubs(String userId) {
        return sqlsession.selectList("archive.getArchiveSubs", userId);
    }

    public int deleteSubTask(String subId) {
        return sqlsession.delete("archive.delSub", subId);
    }

    public int deleteSuperTask(String superId) {
        int superCnt = sqlsession.delete("archive.delSuper", superId);
        String archiveSuperId = sqlsession.selectOne("archive.existArchiveSuperId", superId);
        String taskSuperId = sqlsession.selectOne("archive.existTaskSuperId", superId);
        System.out.println(archiveSuperId + "   " + taskSuperId);
        int subCnt = 0;
        if (superCnt == 1) {
            if (archiveSuperId != null) {
                subCnt = sqlsession.delete("archive.delArchiveSub", superId);
            } // end if
            if (taskSuperId != null) {
                subCnt = sqlsession.delete("archive.delTaskSub", superId);
            } // end if
            System.out.println("삭제 성공!!");
        } else {
            System.out.println("삭제 실패");
        } // end else
        return subCnt;
    }

    public int restoreSubTask(String subId) {
        // 해당 id의 업무정보를 조회하고 vo에 담는다.
        RestoreSubVo vo = new RestoreSubVo();
        vo = sqlsession.selectOne("archive.getRestoreSubId", subId);
        System.out.println("=====" + vo);
        // vo에 담은 정보를 다시 복구시켜준 후
        int insertCnt = sqlsession.insert("archive.restoreSub", vo);
        int delCnt = 0;
        if (insertCnt == 1) { // 복구가 성공하면 해당 테이블에서 지움
            delCnt = sqlsession.delete("archive.delSub", subId);
        } else {
            System.out.println("복구 실패...........");
        }
        return delCnt;
    }

    public int restoreSuperTask(String superId) {
        RestoreSuperVo vo = new RestoreSuperVo();
        System.out.println(superId);
        vo = sqlsession.selectOne("archive.getRestoreSuperId", superId);
        System.out.println("=====" + vo);
        int insertCnt = sqlsession.insert("archive.restoreSuper", vo);
        int delCnt = 0;
        if (insertCnt == 1) { // 복구가 성공하면 해당 테이블에서 지움
            delCnt = sqlsession.delete("archive.delSuper", superId);
        } else {
            System.out.println("복구 실패...........");
        }
        return delCnt;
    }
}