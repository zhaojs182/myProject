<script setup>

import router from './router';

</script>

<template>
<router-view></router-view>
</template>

<style scoped>

</style>

 

 <!-- <template>
    <div id="app">
      <div id="myChart" :style="{ width: width, height: height }"></div>
    </div>
  </template>
  
<script>
  import { defineComponent, ref, onMounted } from 'vue';
  
  export default defineComponent({
    name: 'App',
    data() {
      return {
        width: '100%',  // 可自定义宽度
        height: '400px' // 可自定义高度
      };
    },
    mounted() {
      // 使用 this.$echarts 获取全局注册的 echarts 实例
      const myChart = this.$echarts.init(document.getElementById("myChart"));
      myChart.setOption({
        tooltip: {},
        xAxis: {
          data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
        },
        yAxis: {},
        series: [
          {
            name: "销量",
            type: "bar",
            data: [5, 20, 36, 10, 10, 20]
          }
        ]
      });
    }
  });
</script>
  
<style>
  #myChart {
    width: 100%;
    height: 100%;
  }
</style> -->