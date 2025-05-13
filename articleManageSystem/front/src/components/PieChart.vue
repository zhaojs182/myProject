<!-- src/components/PieChart.vue -->
<template>
    <div ref="chartContainer" :style="{ width: width, height: height }"></div>
  </template>
  
  <script setup>
  import { ref, onMounted, watch } from 'vue';
  import * as echarts from 'echarts/core';
  
  const props = defineProps({
    option: { type: Object, required: true }, // 图表的配置项
    width: { type: String, default: '100%' }, // 图表宽度
    height: { type: String, default: '400px' } // 图表高度
  });
  
  const chartContainer = ref(null);
  let chartInstance = null;
  
  onMounted(() => {
    chartInstance = echarts.init(chartContainer.value); // 初始化 ECharts 实例
    chartInstance.setOption(props.option); // 使用传入的 option 配置项渲染图表
  });
  
  // 监听 option 的变化，更新图表
  watch(() => props.option, (newOption) => {
    if (chartInstance) {
      chartInstance.setOption(newOption); // 如果 option 更新，重新渲染图表
    }
  });
  </script>
  
  <style scoped>
  /* 可选样式，确保图表能够自适应容器 */
  .chart-container {
    width: 100%;
    height: 100%;
  }
  </style>
  