<template>
  <v-card width="95%" class="mx-auto">
    <v-card-title class="text-h5">WEEK TASKS</v-card-title>
    <v-divider></v-divider>
    <v-row>
      <v-col cols="12" md="9">
        <v-simple-table height="300px" class="mx-10">
          <template v-slot:default>
            <thead>
              <tr>
                <th class="text-center" width="150">프로젝트</th>
                <th class="text-center" width="300">업무명</th>
                <th class="text-center" width="120">담당자 수</th>
                <th class="text-center" width="120">진행 상태</th>
                <th class="text-center" width="200">기간</th>
                <th class="text-center" width="80">공개 여부</th>
              </tr>
            </thead>
            <tbody v-if="weekList.length == 0">
              <td colspan="6" class="text-center">NO TASKS</td>
            </tbody>
            <tbody v-else>
              <tr v-for="list in weekList" :key="list.name">
                <td class="text-center">
                  <router-link :to="`/projects/${list.prjId}`">{{ list.prjTitle }}</router-link>
                </td>
                <td class="text-center">[{{ list.ptitle }}]&nbsp;{{ list.ctitle }}</td>
                <v-tooltip bottom color="#5a5a5a">
                  <template v-slot:activator="{ on, attrs }">
                    <!-- 담당자가 1명일때 -->
                    <td class="text-center" style="font-size:12px" v-if="list.managerCount == 1">
                      <v-icon v-bind="attrs" v-on="on">mdi-account</v-icon>
                      &nbsp;({{ list.managerCount }}명)
                    </td>
                    <!-- 담당자가 2명이상일때 -->
                    <td class="text-center" style="font-size:12px" v-if="list.managerCount > 1">
                      <v-icon v-bind="attrs" v-on="on">mdi-account-supervisor</v-icon>
                      &nbsp;({{ list.managerCount }}명)
                    </td>
                  </template>
                  <span font-color="white">{{list.managerName}}</span>
                </v-tooltip>
                <!-- 담당자가 없을 때 -->
                <td class="text-center" v-if="list.managerCount == 0">
                  <v-icon>mdi-account-remove</v-icon>
                </td>
                <td v-if="list.state == 'p'" class="text-center">
                  <v-chip class="ma-2" small color="blue" text-color="white">진행</v-chip>
                </td>
                <td v-if="list.state == 'w'" class="text-center">
                  <v-chip class="ma-2" small color="yellow">대기</v-chip>
                </td>
                <td v-if="list.state == 'h'" class="text-center">
                  <v-chip class="ma-2" small>보류</v-chip>
                </td>
                <td v-if="list.state == 'e'" class="text-center">
                  <v-chip class="ma-2" small color="red" text-color="white">긴급</v-chip>
                </td>
                <td v-if="list.state == 'c'" class="text-center">
                  <v-chip class="ma-2" small color="green" text-color="white">완료</v-chip>
                </td>
                <td class="text-center">{{ list.startDate }} ~ {{ list.endDate }}</td>
                <td v-show="list.usePublic" class="text-center">
                  <v-icon small>mdi-lock-open-variant-outline</v-icon>
                </td>
                <td v-show="!list.usePublic" class="text-center">
                  <v-icon small>mdi-lock-outline</v-icon>
                </td>
              </tr>
            </tbody>
          </template>
        </v-simple-table>
      </v-col>
      <v-divider class="mx-4" vertical></v-divider>
      <v-col cols="12" md="2">
        <p class="text-center mx-3 my-3">WEEK TASKS</p>
        <v-divider class="my-3"></v-divider>
        <div v-if="weekList.length == 0">
          <p class="text-center">NO TASKS</p>
        </div>
        <div class="mx-5" v-else>
          <canvas id="weekChart" width="300" height="300"></canvas>
        </div>
      </v-col>
    </v-row>
  </v-card>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { chartjs } from "../../utils/todoChart.js";

export default {
  data() {
    return {
      chartStateCnt: { h: 0, p: 0, c: 0, w: 0, e: 0 }
    };
  },
  created() {
    this.FETCH_WEEK_DASHBOARD().then(() => {
      for (var i = 0; i < this.weekList.length; i++) {
        // console.log("-----------------------");
        // console.log(this.weekList.length);
        // console.log(this.weekList[i].managerName.indexOf(","));
        // var str = this.weekList[i].managerName.substring(1,this.weekList[i].managerName.indexOf(",")); // 한명 추출
        // console.log(str);
        // var s =this.weekList[i].managerName.substring(1,this.weekList[i].managerName.indexOf(",")) +"님 외 " +this.weekList[i].managerName.match(/,/g).length +"명";
        // console.log(s);
        // if (this.weekList[i].managerName.indexOf(",") != -1) {
        // } //end if

        switch (this.weekList[i].state) {
          case "h":
            this.chartStateCnt.h++;
            break;
          case "p":
            this.chartStateCnt.p++;
            break;
          case "c":
            this.chartStateCnt.c++;
            break;
          case "w":
            this.chartStateCnt.w++;
            break;
          case "e":
            this.chartStateCnt.e++;
            break;
        } // end switch

        // var strArr = this.weekList[i].managerName.replace(,""); // managername 추출
        // console.log('aaaaaa',s);

        // var result = (strArr.match(/,/g)+1);
        // console.log(result);
      } // end for

      const chartObj = {
        type: "doughnut",
        data: {
          datasets: [
            {
              data: [
                this.chartStateCnt.h,
                this.chartStateCnt.p,
                this.chartStateCnt.c,
                this.chartStateCnt.w,
                this.chartStateCnt.e
              ],
              backgroundColor: [
                "#BFC9CA",
                "#5DADE2",
                "#82E0AA",
                "#F7DC6F",
                "#F1948A"
              ],
              borderAlign: "left"
            }
          ],
          // These labels appear in the legend and in the tooltips when hovering different arcs
          labels: ["보류", "진행", "완료", "대기", "긴급"]
        },
        options: {
          /*           title: {
            display: true,
            text: "WEEK TASKS",
            position: "top"
          }, */
          legend: {
            position: "right",
            verticalAlign: "right"
          },
          responsive: false
        },
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true
              }
            }
          ]
        }
      };
      chartjs.createChart("weekChart", chartObj);
    }); // store -> actions
  },
  computed: {
    ...mapState({
      weekList: "weekList"
    })
  },
  methods: {
    ...mapActions(["FETCH_WEEK_DASHBOARD"])
  }
};
</script>

<style></style>