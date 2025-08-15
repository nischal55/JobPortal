<template>
    <div class="flex h-screen">
        <Sidebar />
        <div class="flex-1 flex flex-col w-[69%]">
            <AdminNav />
            <main class="flex-1 p-3 overflow-auto">
                    <div class="text-left text-slate-600 text-lg font-semibold p-5">
                        Job Applications
                    </div>
                    <DataTable :value="applicants" v-model:filters="filters" dataKey="id" paginator :rows="10"
                        :loading="loading" showGridlines filterDisplay="menu" :globalFilterFields="[
                            'seeker.username',
                            'job.title',
                            'job.provider.username',
                            'job.type',
                            'job.location',
                            'status',
                            'coverLetter'
                        ]" class="text-sm">
                        <template #header>
                            <div class="flex justify-between items-center">
                                <Button icon="pi pi-filter-slash" label="Clear" outlined @click="clearFilter" />
                                <IconField>
                                    <InputIcon><i class="pi pi-search" /></InputIcon>
                                    <InputText v-model="filters.global.value" placeholder="Keyword Search" />
                                </IconField>
                            </div>
                        </template>

                        <template #empty>No applicants found.</template>
                        <template #loading>Loading applicants. Please wait.</template>

                        <Column header="S.N">
                            <template #body="{ index }">{{ index + 1 }}</template>
                        </Column>

                        <!-- Applicant Name -->
                        <Column field="seeker.username" header="Applicant Name" style="min-width: 12rem">
                            <template #body="{ data }">{{ data.seeker?.user.username }}</template>
                            <template #filter="{ filterModel }">
                                <InputText v-model="filterModel.value" placeholder="Search by name" class="text-sm" />
                            </template>
                        </Column>

                        <!-- Job Title -->
                        <Column field="job.title" header="Job Title" style="min-width: 12rem">
                            <template #body="{ data }">{{ data.job?.title }}</template>
                            <template #filter="{ filterModel }">
                                <InputText v-model="filterModel.value" placeholder="Search job title" class="text-sm" />
                            </template>
                        </Column>

                        <!-- Job Provider -->
                        <Column field="job.provider.username" header="Provider" style="min-width: 12rem">
                            <template #body="{ data }">{{ data.job?.provider?.username }}</template>
                            <template #filter="{ filterModel }">
                                <InputText v-model="filterModel.value" placeholder="Search provider" class="text-sm" />
                            </template>
                        </Column>

                        <!-- Job Type -->
                        <Column field="job.type" header="Type" style="min-width: 10rem">
                            <template #body="{ data }">{{ data.job?.type }}</template>
                        </Column>

                        <!-- Job Location -->
                        <Column field="job.location" header="Location" style="min-width: 12rem">
                            <template #body="{ data }">{{ data.job?.location }}</template>
                        </Column>

                        <!-- Salary -->
                        <Column field="job.salaryRange" header="Salary" style="min-width: 12rem">
                            <template #body="{ data }">{{ data.job?.salaryRange }}</template>
                        </Column>

                        <!-- Job Deadline -->
                        <Column field="job.deadline" header="Deadline" style="min-width: 12rem">
                            <template #body="{ data }">{{ formatDate(data.job?.deadline) }}</template>
                        </Column>

                        <!-- Status -->
                        <Column field="status" header="Status" style="min-width: 10rem">
                            <template #body="{ data }">{{ data.status }}</template>
                            <template #filter="{ filterModel }">
                                <InputText v-model="filterModel.value" placeholder="Search status" class="text-sm" />
                            </template>
                        </Column>

                        <!-- Resume -->
                    </DataTable>
               
            </main>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Sidebar from '@/components/Sidebar.vue';
import AdminNav from '@/components/AdminNav.vue';
import ApiService from '@/services/ApiService';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';

const applicants = ref([]);
const loading = ref(false);

const FilterMatchMode = { CONTAINS: 'contains' };
const filters = ref({
    global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    'seeker.username': { value: null, matchMode: FilterMatchMode.CONTAINS },
    'job.title': { value: null, matchMode: FilterMatchMode.CONTAINS },
    'job.provider.username': { value: null, matchMode: FilterMatchMode.CONTAINS },
    status: { value: null, matchMode: FilterMatchMode.CONTAINS },
    coverLetter: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

const fetchApplicants = async () => {
    loading.value = true;
    try {
        const res = await ApiService.get('/jobApplicants/findAll');
        applicants.value = res.data;
    } catch (err) {
        console.error('Error fetching applicants:', err);
    } finally {
        loading.value = false;
    }
};

onMounted(fetchApplicants);

const clearFilter = () => {
    for (const key in filters.value) {
        filters.value[key].value = null;
    }
};

const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleDateString();
};
</script>

<style scoped>
.card {
    padding: 2rem;
    margin: 2rem;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
