<template>
  <div class="flex h-screen">
    <!-- Sidebar -->
    <Sidebar />

    <!-- Right Layout: Navbar + Content -->
    <div class="flex-1 flex flex-col w-[69%]">
      <AdminNav/>
      <!-- Main content -->
      <main class="flex-1  p-6 overflow-auto">
        <div class="card">
          <!-- DataTable Component -->
          <DataTable v-model:filters="filters" :value="customers" paginator showGridlines :rows="10" dataKey="id"
            filterDisplay="menu" :loading="loading"
            :globalFilterFields="['name', 'country.name', 'representative.name', 'balance', 'status']">
            <template #header>
              <div class="flex justify-between items-center">
                <Button icon="pi pi-filter-slash" label="Clear" outlined @click="clearFilter" />
                <IconField>
                  <InputIcon><i class="pi pi-search" /></InputIcon>
                  <InputText v-model="filters.global.value" placeholder="Keyword Search" />
                </IconField>
              </div>
            </template>

            <template #empty>No customers found.</template>
            <template #loading>Loading customer data. Please wait.</template>

            <Column field="name" header="Name" style="min-width: 12rem">
              <template #body="{ data }">{{ data.name }}</template>
              <template #filter="{ filterModel }">
                <InputText v-model="filterModel.value" placeholder="Search by name" />
              </template>
            </Column>

            <Column header="Country" filterField="country.name" style="min-width: 12rem">
              <template #body="{ data }">
                <div class="flex items-center gap-2">
                  <img src="https://primefaces.org/cdn/primevue/images/flag/flag_placeholder.png" class="w-6" alt="flag" />
                  <span>{{ data.country.name }}</span>
                </div>
              </template>
              <template #filter="{ filterModel }">
                <InputText v-model="filterModel.value" placeholder="Search by country" />
              </template>
            </Column>

            <Column header="Agent" filterField="representative" :showFilterMatchModes="false"
              :filterMenuStyle="{ width: '14rem' }" style="min-width: 14rem">
              <template #body="{ data }">
                <div class="flex items-center gap-2">
                  <img :src="`https://primefaces.org/cdn/primevue/images/avatar/${data.representative.image}`"
                    class="w-8 h-8" alt="agent" />
                  <span>{{ data.representative.name }}</span>
                </div>
              </template>
              <template #filter="{ filterModel }">
                <MultiSelect v-model="filterModel.value" :options="representatives" optionLabel="name" placeholder="Any">
                  <template #option="slotProps">
                    <div class="flex items-center gap-2">
                      <img :src="`https://primefaces.org/cdn/primevue/images/avatar/${slotProps.option.image}`"
                        class="w-8 h-8" alt="agent" />
                      <span>{{ slotProps.option.name }}</span>
                    </div>
                  </template>
                </MultiSelect>
              </template>
            </Column>

            <Column header="Date" filterField="date" dataType="date" style="min-width: 10rem">
              <template #body="{ data }">{{ formatDate(data.date) }}</template>
              <template #filter="{ filterModel }">
                <DatePicker v-model="filterModel.value" dateFormat="mm/dd/yy" placeholder="mm/dd/yyyy" />
              </template>
            </Column>

            <Column header="Balance" filterField="balance" dataType="numeric" style="min-width: 10rem">
              <template #body="{ data }">{{ formatCurrency(data.balance) }}</template>
              <template #filter="{ filterModel }">
                <InputNumber v-model="filterModel.value" mode="currency" currency="USD" locale="en-US" />
              </template>
            </Column>

            <Column header="Status" field="status" style="min-width: 12rem">
              <template #body="{ data }">
                <Tag :value="data.status" :severity="getSeverity(data.status)" />
              </template>
              <template #filter="{ filterModel }">
                <Select v-model="filterModel.value" :options="statuses" placeholder="Select One" showClear>
                  <template #option="slotProps">
                    <Tag :value="slotProps.option" :severity="getSeverity(slotProps.option)" />
                  </template>
                </Select>
              </template>
            </Column>

            <Column field="activity" header="Activity" :showFilterMatchModes="false" style="min-width: 12rem">
              <template #body="{ data }">
                <ProgressBar :value="data.activity" :showValue="false" style="height: 6px" />
              </template>
              <template #filter="{ filterModel }">
                <Slider v-model="filterModel.value" range class="m-4" />
                <div class="flex justify-between px-2">
                  <span>{{ filterModel.value?.[0] ?? 0 }}</span>
                  <span>{{ filterModel.value?.[1] ?? 100 }}</span>
                </div>
              </template>
            </Column>

            <Column field="verified" header="Verified" dataType="boolean" bodyClass="text-center" style="min-width: 8rem">
              <template #body="{ data }">
                <i class="pi" :class="{
                  'pi-check-circle text-green-500': data.verified,
                  'pi-times-circle text-red-500': !data.verified
                }" />
              </template>
              <template #filter="{ filterModel }">
                <Checkbox v-model="filterModel.value" :indeterminate="filterModel.value === null" binary />
              </template>
            </Column>
          </DataTable>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// PrimeVue Components
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import MultiSelect from 'primevue/multiselect';
import Tag from 'primevue/tag';
import Select from 'primevue/select';
import ProgressBar from 'primevue/progressbar';
import Slider from 'primevue/slider';
import Checkbox from 'primevue/checkbox';
import DatePicker from 'primevue/datepicker';
import InputNumber from 'primevue/inputnumber';
import Sidebar from '@/components/Sidebar.vue';
import AdminNav from '@/components/AdminNav.vue';

// Manual FilterMatchMode constants (avoids import errors)
const FilterMatchMode = {
  CONTAINS: 'contains',
  STARTS_WITH: 'startsWith',
  ENDS_WITH: 'endsWith',
  EQUALS: 'equals',
  NOT_EQUALS: 'notEquals',
  IN: 'in',
  DATE_IS: 'dateIs',
  DATE_BEFORE: 'dateBefore',
  DATE_AFTER: 'dateAfter',
  BETWEEN: 'between',
};

// Sample data for representatives
const representatives = [
  { name: 'Amy Elsner', image: 'amyelsner.png' },
  { name: 'Anna Fali', image: 'annafali.png' },
  { name: 'Asiya Javayant', image: 'asiyajavayant.png' },
  { name: 'Bernardo Dominic', image: 'bernardodominic.png' },
  { name: 'Elwin Sharvill', image: 'elwinsharvill.png' },
  { name: 'Ioni Bowcher', image: 'ionibowcher.png' },
  { name: 'Ivan Magalhaes', image: 'ivanmagalhaes.png' },
  { name: 'Onyama Limba', image: 'onyamalimba.png' },
  { name: 'Stephen Shaw', image: 'stephenshaw.png' },
  { name: 'XuXue Feng', image: 'xuxuefeng.png' },
];

// Status options
const statuses = ['unqualified', 'qualified', 'new', 'negotiation', 'renewal', 'proposal'];

// Generate dummy customer data
const customers = ref(
  Array.from({ length: 50 }).map((_, i) => {
    const rep = representatives[i % representatives.length];
    const status = statuses[i % statuses.length];
    const date = new Date();
    date.setDate(date.getDate() - i * 2);
    return {
      id: i + 1,
      name: `Customer ${i + 1}`,
      country: {
        name: ['USA', 'UK', 'Germany', 'Brazil', 'Canada'][i % 5],
        code: ['us', 'gb', 'de', 'br', 'ca'][i % 5],
      },
      representative: rep,
      date,
      balance: Math.floor(Math.random() * 10000),
      status,
      activity: Math.floor(Math.random() * 100),
      verified: i % 2 === 0,
    };
  }),
);

const loading = ref(false);

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  name: { value: null, matchMode: FilterMatchMode.CONTAINS },
  'country.name': { value: null, matchMode: FilterMatchMode.CONTAINS },
  representative: { value: null, matchMode: FilterMatchMode.IN },
  date: { value: null, matchMode: FilterMatchMode.DATE_IS },
  balance: { value: null, matchMode: FilterMatchMode.EQUALS },
  status: { value: null, matchMode: FilterMatchMode.EQUALS },
  activity: { value: null, matchMode: FilterMatchMode.BETWEEN },
  verified: { value: null, matchMode: FilterMatchMode.EQUALS },
});

const formatDate = (date) => new Date(date).toLocaleDateString('en-US');

const formatCurrency = (value) =>
  value.toLocaleString('en-US', {
    style: 'currency',
    currency: 'USD',
  });

const getSeverity = (status) => {
  switch (status) {
    case 'unqualified':
      return 'danger';
    case 'qualified':
      return 'success';
    case 'new':
      return 'info';
    case 'negotiation':
      return 'warning';
    case 'renewal':
      return null;
    case 'proposal':
      return 'info';
    default:
      return null;
  }
};

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
