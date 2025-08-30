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
import { ref } from 'vue'
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

// Chart Components
const DoughnutChart = Doughnut
const LineChart = Line

// Top Stats Cards
const statsCards = ref([
    { title: 'Job Providers', value: '245', trend: 5.2, icon: 'pi pi-address-book' },
    { title: 'Job Seekers', value: '1,456', trend: 8.4, icon: 'pi pi-users' },
    { title: 'Applicants', value: '890', trend: 3.1, icon: 'pi pi-list-check' },
    { title: 'Jobs Posted', value: '320', trend: 6.7, icon: 'pi pi-briefcase' },
])

// ✅ Make chart data reactive
const applicationChartData = ref({
    labels: ['Accepted', 'Pending', 'Rejected'],
    datasets: [
        {
            data: [320, 420, 150],
            backgroundColor: ['#10b981', '#facc15', '#ef4444'],
            hoverOffset: 8,
        },
    ],
})

const jobChartData = ref({
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug'],
    datasets: [
        {
            label: 'Jobs Posted',
            data: [30, 50, 45, 70, 60, 80, 65, 90],
            borderColor: '#039e27',
            backgroundColor: '#039e27',
            fill: true,
            tension: 0.3,
        },
    ],
})

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

// Applications
const applications = ref([
    {
        name: 'John Doe',
        avatar: 'https://i.pravatar.cc/30?u=john',
        job: 'Frontend Developer',
        date: '21/07/2022 08:21',
        status: 'Accepted',
    },
    {
        name: 'Jane Smith',
        avatar: 'https://i.pravatar.cc/30?u=jane',
        job: 'UI/UX Designer',
        date: '22/07/2022 10:15',
        status: 'Pending',
    },
    {
        name: 'Alex Johnson',
        avatar: 'https://i.pravatar.cc/30?u=alex',
        job: 'Backend Developer',
        date: '23/07/2022 11:00',
        status: 'Rejected',
    },
    {
        name: 'Emily Brown',
        avatar: 'https://i.pravatar.cc/30?u=emily',
        job: 'Project Manager',
        date: '23/07/2022 12:45',
        status: 'Pending',
    },
])
</script>
