<!-- src/components/Chart.vue -->
<!-- <template>
  <div>
    <BarChart :option="barChartOption" />
    <LineChart :option="lineChartOption" />
    <PieChart :option="pieChartOption" />
    <ScatterChart :option="scatterChartOption" />
  </div>
</template> -->
<template>
  <div class="chart-container">
    <div class="chart-block">
      <BarChart :option="barChartOption" />
    </div>
    <div class="chart-block">
      <LineChart :option="lineChartOption" />
    </div>
    <div class="chart-block">
      <PieChart :option="pieChartOption" />
    </div>
    <div class="chart-block">
      <ScatterChart :option="scatterChartOption" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import BarChart from '@/components/BarChart.vue'; // 引入 BarChart 组件
import LineChart from '@/components/LineChart.vue'; // 引入 LineChart 组件
import PieChart from '@/components/PieChart.vue'; // 引入 PieChart 组件
import ScatterChart from '@/components/ScatterChart.vue'; // 引入 ScatterChart 组件
import { categoryFrequency } from '@/api/chart.js'; // 假设是一个获取图表数据的 API

const barChartOption = ref({});
const lineChartOption = ref({});
const pieChartOption = ref({});
const scatterChartOption = ref({});

const loadChartData = async () => {
  const response = await categoryFrequency(); // 假设从 API 获取到数据
  console.log("这是使用图表的后端返回数据", response);

  // 提取 categoryUsageDTOS 数据
  const categoryUsageDTOS = response.data.data.categoryUsageDTOS;
  
  // 获取分类名称 (X 轴数据) 和 文章数量 (Y 轴数据)
  const categories = categoryUsageDTOS.map(item => item.categoryName);
  const sales = categoryUsageDTOS.map(item => item.articleCount);

  // 设置柱状图配置项
  barChartOption.value = {
    title: { text: '文章类别使用频率（柱状图）' },
    tooltip: {},
    xAxis: {
      data: categories // 设置 X 轴为类别名称
    },
    yAxis: {},
    series: [
      {
        name: '文章数量',
        type: 'bar', // 设置为柱状图
        data: sales // 设置 Y 轴为文章数量
      }
    ]
  };

  // 设置折线图配置项
  lineChartOption.value = {
    title: { text: '文章类别使用频率（折线图）' },
    tooltip: {
      trigger: 'axis',
    },
    legend: {
      data: ['文章数量']
    },
    xAxis: {
      type: 'category',
      data: categories // 设置 X 轴为类别名称
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '文章数量',
        type: 'line', // 设置为折线图
        data: sales // 设置 Y 轴为文章数量
      }
    ]
  };

  // 设置饼图配置项
  pieChartOption.value = {
    title: { text: '文章类别使用频率（饼图）', left: 'center' },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)' // 设置格式
    },
    series: [
      {
        name: '文章数量',
        type: 'pie', // 设置为饼图
        radius: '55%', // 设置饼图的半径
        data: categories.map((category, index) => ({
          value: sales[index],
          name: category // 饼图每一项的名称为分类名称
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };

  // 设置散点图配置项
  scatterChartOption.value = {
    title: { text: '文章类别使用频率（散点图）' },
    tooltip: {
      trigger: 'item',
      formatter: '类别: {b}<br/>文章数量: {c}'
    },
    xAxis: {
      type: 'category',
      data: categories // X 轴为分类名称
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '文章数量',
        type: 'scatter', // 设置为散点图
        data: categories.map((category, index) => [category, sales[index]]) // X 为分类，Y 为文章数量
      }
    ]
  };
};

onMounted(() => {
  loadChartData();
});
</script>

<!-- <style scoped>
/* 可选样式，确保图表能够自适应容器 */
.chart-container {
  width: 100%;
  height: 100%;
}
</style> -->

<style scoped>
.chart-container {
  display: flex;             /* 使用 flex 布局 */
  flex-wrap: wrap;           /* 使图表容器换行显示 */
  gap: 20px;                 /* 图表之间的间距 */
  justify-content: space-between; /* 图表均匀分布 */
  padding: 20px;             /* 给容器添加内边距 */
  box-sizing: border-box;    /* 防止溢出 */
}

.chart-block {
  flex: 1 1 45%;             /* 每个图表占 45% 宽度，并且自适应 */
  max-width: 600px;          /* 控制最大宽度 */
  height: 300px;             /* 设置每个图表的高度 */
  border: 1px solid #ddd;    /* 可选，给每个图表添加边框 */
  border-radius: 8px;        /* 圆角效果 */
  background-color: #fff;    /* 背景色 */
  box-sizing: border-box;    /* 确保 padding 不影响图表的尺寸 */
  overflow: hidden;          /* 确保内容不超出 */
}

.chart-block .echarts {
  width: 100%;
  height: 100%;
}
</style>