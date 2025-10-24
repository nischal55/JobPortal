<template>
    <div class="flex h-screen">
        <Sidebar />
        <div class="flex-1 flex flex-col w-[69%] pb-10">
            <AdminNav />

            <main class="bg-gray-50 dark:bg-surface-950 p-6 overflow-auto">
                <!-- Top Stats -->
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
                    <div v-for="card in statsCards" :key="card.title"
                        class="bg-white dark:bg-surface-900 p-5 rounded-xl shadow-md flex flex-col">
                        <div class="flex justify-between items-center">
                            <div>
                                <p class="text-gray-500 text-sm">{{ card.title }}</p>
                                <h2 class="text-2xl font-semibold text-gray-900 dark:text-gray-100">
                                    {{ card.value }}
                                </h2>
                            </div>
                            <div
                                class="w-10 h-10 flex items-center justify-center rounded-lg bg-emerald-100 text-emerald-600">
                                <i :class="card.icon" />
                            </div>
                        </div>
                        <p class="mt-2 text-xs" :class="card.trend > 0 ? 'text-emerald-600' : 'text-red-500'">
                            {{ card.trend > 0 ? '+' : '' }}{{ card.trend }}% Since last week
                        </p>
                    </div>
                </div>

                <!-- Middle Section -->
                <div class="grid grid-cols-1 lg:grid-cols-6 gap-6 mb-6">
                    <!-- Application Stats -->
                    <div class="bg-white dark:bg-surface-900 p-6 lg:col-span-2 rounded-xl shadow-md">
                        <h3 class="text-gray-700 dark:text-gray-200 font-medium mb-4">Application Statistics</h3>
                        <div class="h-72"> <!-- 👈 control height -->
                            <DoughnutChart :data="applicationChartData" :options="chartOptions" />
                        </div>
                    </div>

                    <!-- Job Posting Analytics -->
                    <div class="bg-white dark:bg-surface-900 p-6 lg:col-span-4 rounded-xl shadow-md">
                        <h3 class="text-gray-700 dark:text-gray-200 font-medium mb-4">Job Posting Analytics</h3>
                        <LineChart :data="jobChartData" :options="chartOptions" />
                    </div>
                </div>
            </main>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ApiService from '@/services/ApiService'
import AdminNav from '@/components/AdminNav.vue'
import Sidebar from '@/components/Sidebar.vue'

import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale
} from 'chart.js'
import { Doughnut, Line } from 'vue-chartjs'

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  ArcElement,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale
)

const DoughnutChart = Doughnut
const LineChart = Line

// ---------- Default Dashboard Stats ----------
const statsCards = ref([
  { title: 'Job Providers', value: 0, trend: 5.2, icon: 'pi pi-address-book' },
  { title: 'Job Seekers', value: 0, trend: 8.4, icon: 'pi pi-users' },
  { title: 'Applicants', value: 0, trend: 3.1, icon: 'pi pi-list-check' },
  { title: 'Jobs Posted', value: 0, trend: 6.7, icon: 'pi pi-briefcase' },
])

// ---------- Chart Data (Reactive) ----------
const applicationChartData = ref({
  labels: ['Applicants', 'Job Providers', 'Job Seekers', 'Jobs Posted'],
  datasets: [
    {
      data: [0, 0, 0, 0],
      backgroundColor: ['#10b981', '#3b82f6', '#facc15', '#ef4444'],
      hoverOffset: 8,
    },
  ],
})

const jobChartData = ref({
  labels: ['Applicants', 'Providers', 'Seekers', 'Jobs'],
  datasets: [
    {
      label: 'Platform Statistics',
      data: [0, 0, 0, 0],
      borderColor: '#039e27',
      backgroundColor: 'rgba(3, 158, 39, 0.4)',
      fill: true,
      tension: 0.3,
    },
  ],
})

// ---------- Chart Options ----------
const chartOptions = ref({
  responsive: true,
  plugins: {
    legend: {
      position: 'bottom',
      labels: { color: '#6b7280' },
    },
  },
  scales: {
    x: { ticks: { color: '#6b7280' } },
    y: { ticks: { color: '#6b7280' } },
  },
})

// ---------- Fetch Data from API ----------
const fetchDashboardCounts = async () => {
  try {
    const [applicantsRes, providersRes, seekerRes, jobRes] = await Promise.all([
      ApiService.get('/jobApplicants/getApplicantsCount'),
      ApiService.get('/jobproviders/getJobProviderCount'),
      ApiService.get('/jobseeker/getJobSeekerCount'),
      ApiService.get('/jobDetail/findJobCount')
    ])

    // Extract numeric counts (assuming plain integer responses)
    const applicantsCount = applicantsRes.data
    const providersCount = providersRes.data
    const seekerCount = seekerRes.data
    const jobCount = jobRes.data

    // Update Top Stats
    statsCards.value = statsCards.value.map(card => {
      if (card.title === 'Applicants') return { ...card, value: applicantsCount }
      if (card.title === 'Job Providers') return { ...card, value: providersCount }
      if (card.title === 'Job Seekers') return { ...card, value: seekerCount }
      if (card.title === 'Jobs Posted') return { ...card, value: jobCount }
      return card
    })

    // ---------- Update Chart Data Based on Counts ----------
    applicationChartData.value = {
      labels: ['Applicants', 'Job Providers', 'Job Seekers', 'Jobs Posted'],
      datasets: [
        {
          data: [applicantsCount, providersCount, seekerCount, jobCount],
          backgroundColor: ['#10b981', '#3b82f6', '#facc15', '#ef4444'],
          hoverOffset: 8,
        },
      ],
    }

    jobChartData.value = {
      labels: ['Applicants', 'Providers', 'Seekers', 'Jobs'],
      datasets: [
        {
          label: 'Platform Statistics',
          data: [applicantsCount, providersCount, seekerCount, jobCount],
          borderColor: '#039e27',
          backgroundColor: 'rgba(3, 158, 39, 0.4)',
          fill: true,
          tension: 0.3,
        },
      ],
    }
  } catch (error) {
    console.error('Failed to fetch dashboard stats:', error)
  }
}

// Fetch data when component mounts
onMounted(fetchDashboardCounts)
</script>
