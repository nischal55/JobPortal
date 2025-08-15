<template>
    <div class="flex h-screen">
        <Sidebar />
        <div class="flex-1 flex flex-col w-[69%]">
            <AdminNav />
            <div class="bg-surface-50 dark:bg-surface-950 px-6 py-8 md:px-12 lg:px-20">
                <div class="grid grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-4">

                    <!-- Job Providers -->
                    <div class="bg-surface-0 dark:bg-surface-900 shadow-sm p-5 rounded-2xl">
                        <div class="flex justify-between gap-4">
                            <div class="flex flex-col gap-2">
                                <span class="text-surface-700 dark:text-surface-300 font-normal leading-tight">Job Providers</span>
                                <div class="text-surface-900 dark:text-surface-0 font-semibold !text-2xl !leading-tight">
                                    {{ counts.providers }}
                                </div>
                            </div>
                            <div class="flex items-center text-emerald-600 justify-center rounded-lg w-10 h-10">
                                <i class="pi pi-address-book text-surface-0 dark:text-surface-900 !text-xl !leading-none" />
                            </div>
                        </div>
                        <div class="mt-4">
                            <span class="text-surface-600 dark:text-surface-300 font-medium leading-tight">24 new</span>
                            <span class="text-surface-500 dark:text-surface-300 leading-tight"> since last visit</span>
                        </div>
                    </div>

                    <!-- Job Seekers -->
                    <div class="bg-surface-0 dark:bg-surface-900 shadow-sm p-5 rounded-2xl">
                        <div class="flex justify-between gap-4">
                            <div class="flex flex-col gap-2">
                                <span class="text-surface-700 dark:text-surface-300 font-normal leading-tight">Job Seekers</span>
                                <div class="text-surface-900 dark:text-surface-0 font-semibold !text-2xl !leading-tight">
                                    {{ counts.seekers }}
                                </div>
                            </div>
                            <div class="flex items-center justify-center text-emerald-600 rounded-lg w-10 h-10">
                                <i class="pi pi-users text-surface-0 dark:text-surface-900 !text-xl !leading-none" />
                            </div>
                        </div>
                        <div class="mt-4">
                            <span class="text-surface-600 dark:text-surface-300 font-medium leading-tight">48 new</span>
                            <span class="text-surface-500 dark:text-surface-300 leading-tight"> since last visit</span>
                        </div>
                    </div>

                    <!-- Applicants -->
                    <div class="bg-surface-0 dark:bg-surface-900 shadow-sm p-5 rounded-2xl">
                        <div class="flex justify-between gap-4">
                            <div class="flex flex-col gap-2">
                                <span class="text-surface-700 dark:text-surface-300 font-normal leading-tight">Applicants</span>
                                <div class="text-surface-900 dark:text-surface-0 font-semibold !text-2xl !leading-tight">
                                    {{ counts.applicants }}
                                </div>
                            </div>
                            <div class="flex items-center justify-center text-emerald-600 rounded-lg w-10 h-10">
                                <i class="pi pi-list-check text-surface-0 dark:text-surface-900 !text-xl !leading-none" />
                            </div>
                        </div>
                        <div class="mt-4">
                            <span class="text-surface-500 dark:text-surface-300 leading-tight">32,56 / 250 GB</span>
                        </div>
                    </div>

                    <!-- Job Posted -->
                    <div class="bg-surface-0 dark:bg-surface-900 shadow-sm p-5 rounded-2xl">
                        <div class="flex justify-between gap-4">
                            <div class="flex flex-col gap-2">
                                <span class="text-surface-700 dark:text-surface-300 font-normal leading-tight">Job Posted</span>
                                <div class="text-surface-900 dark:text-surface-0 font-semibold !text-2xl !leading-tight">
                                    {{ counts.jobs }}
                                </div>
                            </div>
                            <div class="flex items-center justify-center text-emerald-600 rounded-lg w-10 h-10">
                                <i class="pi pi-briefcase text-surface-0 dark:text-surface-900 !text-xl !leading-none" />
                            </div>
                        </div>
                        <div class="mt-4">
                            <span class="text-surface-600 dark:text-surface-300 font-medium leading-tight">72 new</span>
                            <span class="text-surface-500 dark:text-surface-300 leading-tight"> user this week</span>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AdminNav from '@/components/AdminNav.vue'
import Sidebar from '@/components/Sidebar.vue'
import ApiService from '@/services/ApiService' // your axios wrapper

const counts = ref({
    providers: 0,
    seekers: 0,
    applicants: 0,
    jobs: 0
})

const fetchCounts = async () => {
    try {
        const [providersRes, seekersRes, applicantsRes, jobsRes] = await Promise.all([
            ApiService.get('/jobproviders/findAll'),
            ApiService.get('/jobseeker/findAll'),
            ApiService.get('/jobApplicants/findAll'),
            ApiService.get('/jobDetail/findAll')
        ])

        counts.value.providers = providersRes.data.length
        counts.value.seekers = seekersRes.data.length
        counts.value.applicants = applicantsRes.data.length
        counts.value.jobs = jobsRes.data.length

    } catch (error) {
        console.error('Error fetching counts:', error)
    }
}

onMounted(fetchCounts)
</script>
