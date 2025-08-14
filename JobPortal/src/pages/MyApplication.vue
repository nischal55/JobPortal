<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import ApiService from '@/services/ApiService';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Dropdown from 'primevue/dropdown';
import Navbar from '@/components/navbar.vue';

const router = useRouter();
const jobApplications = ref([]);
const loading = ref(false);

// Filters
const FilterMatchMode = {
    CONTAINS: 'contains',
    EQUALS: 'equals',
};

const filters = ref({
    global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    'seeker.user.username': { value: null, matchMode: FilterMatchMode.CONTAINS },
    'job.title': { value: null, matchMode: FilterMatchMode.CONTAINS },
    status: { value: null, matchMode: FilterMatchMode.EQUALS },
});

const statusOptions = [
    { label: 'Applied', value: 'applied' },
    { label: 'Reviewing', value: 'reviewing' },
    { label: 'Rejected', value: 'rejected' },
    { label: 'Accepted', value: 'accepted' },
];

const userId = localStorage.getItem('user_id');

const fetchJobApplications = async () => {
    loading.value = true;
    try {
        const response = await ApiService.get(`/jobApplicants/findApplicantsByUserId/${userId}`);
        jobApplications.value = response.data || [];
    } catch (err) {
        console.error('Error fetching job applications:', err);
    } finally {
        loading.value = false;
    }
};

onMounted(fetchJobApplications);

const clearFilter = () => {
    filters.value.global.value = null;
    filters.value['seeker.user.username'].value = null;
    filters.value['job.title'].value = null;
    filters.value.status.value = null;
};

// Actions
const viewApplication = (application) => {
    router.push({ name: 'job-application-view', params: { id: application.id } });
};

const updateApplication = (application) => {
    router.push({ name: 'job-application-edit', params: { id: application.id } });
};
</script>

<template>
    <Navbar />
    <div class="card">
        <div class="text-center text-xl font-bold">
            My Applications
        </div>
        <DataTable :value="jobApplications" v-model:filters="filters" paginator :rows="10" :loading="loading"
            filterDisplay="menu" :globalFilterFields="['seeker.user.username', 'job.title', 'status']" dataKey="id">
            <template #header>
                <div class="flex justify-between items-center mb-2">
                    <Button label="Clear Filters" icon="pi pi-filter-slash" outlined @click="clearFilter" />
                    <InputText v-model="filters.global.value" placeholder="Global Search" class="p-inputtext-sm" />
                </div>
            </template>

            <Column header="S.N">
                <template #body="{ index }">{{ index + 1 }}</template>
            </Column>
            
            <Column header="Applied At" style="min-width: 12rem">
                <template #body="{ data }">{{ new Date(data.appliedAt).toLocaleString() }}</template>
            </Column>

            <Column field="seeker.user.username" header="Applicant Name" :filter="true" style="min-width: 12rem">
                <template #body="{ data }">{{ data.seeker?.user?.username }}</template>
                <template #filter="{ filterModel }">
                    <InputText v-model="filterModel.value" placeholder="Search by name" class="p-inputtext-sm" />
                </template>
            </Column>

            <Column field="job.title" header="Job Title" :filter="true" style="min-width: 12rem">
                <template #body="{ data }">{{ data.job?.title }}</template>
                <template #filter="{ filterModel }">
                    <InputText v-model="filterModel.value" placeholder="Search by job title" class="p-inputtext-sm" />
                </template>
            </Column>

            <Column field="status" header="Status" :filter="true" style="min-width: 10rem">
                <template #body="{ data }">
                    <span :class="[
                        'px-5 rounded-full text-white font-semibold',
                        data.status === 'applied' ? 'bg-yellow-300' :
                            data.status === 'reviewing' ? 'bg-blue-200' :
                                data.status === 'accepted' ? 'bg-green-200' :
                                    data.status === 'rejected' ? 'bg-red-200' : ''
                    ]">
                        {{ data.status }}
                    </span>
                </template>
                <template #filter="{ filterModel }">
                    <Dropdown v-model="filterModel.value" :options="statusOptions" optionLabel="label"
                        optionValue="value" placeholder="Select Status" />
                </template>
            </Column>

            <Column header="Actions" style="min-width: 12rem">
                <template #body="{ data }">
                    <div class="flex gap-2">
                        <Button label="View" icon="pi pi-eye" outlined size="small" @click="viewApplication(data)" />
                        <Button label="Update" icon="pi pi-pencil" severity="warning" size="small"
                            @click="updateApplication(data)" />
                    </div>
                </template>
            </Column>
        </DataTable>
    </div>
</template>

<style scoped>
.card {
    padding: 1.5rem;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
