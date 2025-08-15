<template>
    <div class="flex h-screen">
        <Sidebar />
        <div class="flex-1 flex flex-col w-[69%]">
            <AdminNav />
            <main class="flex-1 p-3 overflow-auto">
                <div class="card">
                    <div class="text-center text-slate-600 text-lg font-semibold p-5">
                        Job Provider
                    </div>
                    <DataTable :value="providers" v-model:filters="filters" dataKey="id" paginator :rows="10"
                        :loading="loading" showGridlines filterDisplay="menu" :globalFilterFields="[
                            'user.username',
                            'companyName',
                            'website',
                            'contactNo',
                            'contactEmail',
                            'description'
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

                        <template #empty>No providers found.</template>
                        <template #loading>Loading providers. Please wait.</template>

                        <Column header="S.N">
                            <template #body="{ index }">{{ index + 1 }}</template>
                        </Column>

                        <Column field="user.username" header="Username" style="min-width: 12rem">
                            <template #body="{ data }">{{ data.user?.username }}</template>
                            <template #filter="{ filterModel }">
                                <InputText v-model="filterModel.value" placeholder="Search username" class="text-sm" />
                            </template>
                        </Column>

                        <Column field="companyName" header="Company Name" style="min-width: 12rem">
                            <template #body="{ data }">{{ data.companyName }}</template>
                            <template #filter="{ filterModel }">
                                <InputText v-model="filterModel.value" placeholder="Search company" class="text-sm" />
                            </template>
                        </Column>

                        <Column field="website" header="Website" style="min-width: 12rem">
                            <template #body="{ data }">
                                <a v-if="data.website" :href="data.website" target="_blank"
                                    class="text-blue-500 underline">{{ data.website }}</a>
                            </template>
                            <template #filter="{ filterModel }">
                                <InputText v-model="filterModel.value" placeholder="Search website" class="text-sm" />
                            </template>
                        </Column>

                        <Column field="contactNo" header="Contact No" style="min-width: 12rem">
                            <template #body="{ data }">{{ data.contactNo }}</template>
                        </Column>

                        <Column field="contactEmail" header="Contact Email" style="min-width: 14rem">
                            <template #body="{ data }">{{ data.contactEmail }}</template>
                        </Column>

                        <Column field="description" header="Description" style="min-width: 14rem">
                            <template #body="{ data }">{{ data.description }}</template>
                        </Column>
                    </DataTable>
                </div>
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

const providers = ref([]);
const loading = ref(false);

const FilterMatchMode = { CONTAINS: 'contains' };
const filters = ref({
    global: { value: null, matchMode: FilterMatchMode.CONTAINS },
    'user.username': { value: null, matchMode: FilterMatchMode.CONTAINS },
    companyName: { value: null, matchMode: FilterMatchMode.CONTAINS },
    website: { value: null, matchMode: FilterMatchMode.CONTAINS },
    contactNo: { value: null, matchMode: FilterMatchMode.CONTAINS },
    contactEmail: { value: null, matchMode: FilterMatchMode.CONTAINS },
    description: { value: null, matchMode: FilterMatchMode.CONTAINS },
});

const fetchProviders = async () => {
    loading.value = true;
    try {
        const res = await ApiService.get('/jobproviders/findAll');
        providers.value = res.data;
    } catch (err) {
        console.error('Error fetching providers:', err);
    } finally {
        loading.value = false;
    }
};

onMounted(fetchProviders);

const clearFilter = () => {
    for (const key in filters.value) {
        filters.value[key].value = null;
    }
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
