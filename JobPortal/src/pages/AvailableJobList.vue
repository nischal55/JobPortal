<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import ApiService from '@/services/ApiService';
import InputText from 'primevue/inputtext';
import Dropdown from 'primevue/dropdown';
import Button from 'primevue/button';
import Card from 'primevue/card';
import Badge from 'primevue/badge';
import Skeleton from 'primevue/skeleton';
import Navbar from '@/components/navbar.vue';
import { useRoute } from 'vue-router'
const route = useRoute()

const router = useRouter();
const jobs = ref([]);
const loading = ref(false);

// Filters
const searchKeyword = ref('');
const locationFilter = ref('');
const typeFilter = ref('');

const jobTypes = [
  { label: 'Full Time', value: 'full_time' },
  { label: 'Part Time', value: 'part_time' },
  { label: 'Internship', value: 'internship' },
  { label: 'Contract', value: 'contract' },
];

// Fetch jobs from API
const fetchJobs = async () => {
  loading.value = true;
  try {
    const response = await ApiService.get('/jobDetail/findAll');
    jobs.value = response.data || [];
  } catch (err) {
    console.error('Error fetching jobs:', err);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  if (route.query.q) {
    searchKeyword.value = route.query.q
  }
  fetchJobs()
})

// Computed filtered jobs
const filteredJobs = computed(() => {
  return jobs.value.filter(job => {
    const matchesKeyword =
      job.title.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
      job.provider?.username?.toLowerCase().includes(searchKeyword.value.toLowerCase());

    const matchesLocation = locationFilter.value
      ? job.location.toLowerCase().includes(locationFilter.value.toLowerCase())
      : true;

    const matchesType = typeFilter.value ? job.type === typeFilter.value : true;

    return matchesKeyword && matchesLocation && matchesType;
  });
});

// View job details
const viewJobDetails = (job) => {
  router.push({ name: 'job-view', params: { id: job.id } });
};

const clearFilters = () => {
  searchKeyword.value = '';
  locationFilter.value = '';
  typeFilter.value = '';
};

// Helper function to get job type styling
const getJobTypeStyle = (type) => {
  const styles = {
    full_time: 'bg-green-100 text-green-800',
    part_time: 'bg-blue-100 text-blue-800',
    internship: 'bg-purple-100 text-purple-800',
    contract: 'bg-orange-100 text-orange-800'
  };
  return styles[type] || 'bg-gray-100 text-gray-800';
};

// Helper function to format job type display
const formatJobType = (type) => {
  return jobTypes.find(jt => jt.value === type)?.label || type;
};

// Helper function to check if deadline is approaching
const isDeadlineApproaching = (deadline) => {
  const today = new Date();
  const deadlineDate = new Date(deadline);
  const diffTime = deadlineDate - today;
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  return diffDays <= 7 && diffDays > 0;
};
</script>

<template>
  <Navbar/>
  <div class="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50">
    <!-- Header Section -->
    <div class="bg-emerald-600 shadow-sm border-b mb-10">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div class="text-center">
          <h1 class="text-2xl font-bold text-gray-900 mb-2 text-white">Find Your Dream Job</h1>
          <p class="text-gray-600 text-md text-white">Discover opportunities that match your skills and aspirations</p>
        </div>
      </div>
    </div>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div class="flex flex-col lg:flex-row gap-8">
        <!-- Enhanced Sidebar Filters -->
        <div class="lg:w-80">
          <Card class="sticky top-4">
            <template #header>
              <div class="px-6 py-4 border-b">
                <div class="flex items-center justify-between">
                  <h3 class="text-lg font-semibold text-gray-900 flex items-center">
                    <i class="pi pi-filter mr-2 text-blue-600"></i>
                    Filters
                  </h3>
                  <Badge 
                    v-if="filteredJobs.length" 
                    :value="filteredJobs.length" 
                    class="bg-blue-100 text-blue-800"
                  />
                </div>
              </div>
            </template>
            <template #content>
              <div class="px-6 pb-6 space-y-6">
                <div class="space-y-2">
                  <label class="block text-sm font-medium text-gray-700">
                    <i class="pi pi-search mr-2"></i>Search Keywords
                  </label>
                  <InputText 
                    v-model="searchKeyword" 
                    placeholder="Job title or company..." 
                    class="w-full"
                  />
                </div>
                
                <div class="space-y-2">
                  <label class="block text-sm font-medium text-gray-700">
                    <i class="pi pi-map-marker mr-2"></i>Location
                  </label>
                  <InputText 
                    v-model="locationFilter" 
                    placeholder="City, state, or remote..." 
                    class="w-full"
                  />
                </div>
                
                <div class="space-y-2">
                  <label class="block text-sm font-medium text-gray-700">
                    <i class="pi pi-briefcase mr-2"></i>Job Type
                  </label>
                  <Dropdown 
                    v-model="typeFilter" 
                    :options="jobTypes" 
                    optionLabel="label" 
                    optionValue="value" 
                    placeholder="Select job type..." 
                    class="w-full"
                    showClear
                  />
                </div>
                
                <Button 
                  label="Clear All Filters" 
                  icon="pi pi-times"
                  outlined 
                  severity="secondary"
                  class="w-full mt-4" 
                  @click="clearFilters"
                  :disabled="!searchKeyword && !locationFilter && !typeFilter"
                />
              </div>
            </template>
          </Card>
        </div>

        <!-- Enhanced Job Cards Section -->
        <div class="flex-1">
          <!-- Loading State -->
          <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
            <Card v-for="n in 6" :key="n" class="overflow-hidden">
              <template #content>
                <div class="p-6 space-y-4">
                  <Skeleton height="1.5rem" class="mb-2"></Skeleton>
                  <Skeleton height="1rem" width="70%"></Skeleton>
                  <Skeleton height="1rem" width="50%"></Skeleton>
                  <div class="flex justify-between">
                    <Skeleton height="1rem" width="40%"></Skeleton>
                    <Skeleton height="1rem" width="30%"></Skeleton>
                  </div>
                  <Skeleton height="2.5rem"></Skeleton>
                </div>
              </template>
            </Card>
          </div>

          <!-- No Jobs State -->
          <div v-else-if="filteredJobs.length === 0" class="text-center py-16">
            <div class="mx-auto w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mb-4">
              <i class="pi pi-search text-3xl text-gray-400"></i>
            </div>
            <h3 class="text-lg font-medium text-gray-900 mb-2">No jobs found</h3>
            <p class="text-gray-500 mb-6">Try adjusting your filters or search terms</p>
            <Button label="Clear Filters" icon="pi pi-refresh" @click="clearFilters" />
          </div>

          <!-- Job Cards Grid -->
          <div v-else>
            <div class="flex justify-between items-center mb-6">
              <h2 class="text-xl font-semibold text-gray-900">
                {{ filteredJobs.length }} {{ filteredJobs.length === 1 ? 'Job' : 'Jobs' }} Found
              </h2>
            </div>
            
            <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
              <Card 
                v-for="job in filteredJobs" 
                :key="job.id" 
                class="group hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1 cursor-pointer border-l-4 border-l-blue-500"
                @click="viewJobDetails(job)"
              >
                <template #content>
                  <div class="p-6">
                    <!-- Header -->
                    <div class="flex justify-between items-start mb-4">
                      <div class="flex-1">
                        <h3 class="font-bold text-lg text-gray-900 mb-2 group-hover:text-blue-600 transition-colors line-clamp-2">
                          {{ job.title }}
                        </h3>
                        <p class="text-gray-600 font-medium">
                          <i class="pi pi-building mr-1"></i>
                          {{ job.provider?.username }}
                        </p>
                      </div>
                      <Badge 
                        :value="formatJobType(job.type)"
                        :class="getJobTypeStyle(job.type)"
                        class="text-xs font-medium px-3 py-1 rounded-full"
                      />
                    </div>

                    <!-- Details -->
                    <div class="space-y-3 mb-6">
                      <div class="flex items-center text-gray-600">
                        <i class="pi pi-map-marker mr-2 text-gray-400"></i>
                        <span class="text-sm">{{ job.location }}</span>
                      </div>
                      
                      <div class="flex items-center text-gray-600">
                        <i class="pi pi-dollar mr-2 text-gray-400"></i>
                        <span class="text-sm font-medium">
                          {{ job.salaryRange || 'Salary not disclosed' }}
                        </span>
                      </div>
                      
                      <div class="flex items-center">
                        <i class="pi pi-calendar mr-2 text-gray-400"></i>
                        <span class="text-sm text-gray-600">
                          Deadline: {{ new Date(job.deadline).toLocaleDateString() }}
                        </span>
                        <Badge 
                          v-if="isDeadlineApproaching(job.deadline)"
                          value="Urgent"
                          severity="danger"
                          class="ml-2 text-xs"
                        />
                      </div>
                    </div>

                    <!-- Action Button -->
                    <Button 
                      label="View Details" 
                      icon="pi pi-arrow-right" 
                      iconPos="right"
                      class="w-full group-hover:bg-blue-700 transition-colors"
                      @click.stop="viewJobDetails(job)"
                    />
                  </div>
                </template>
              </Card>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* Custom scrollbar for sidebar */
.sticky {
  position: sticky;
  top: 1rem;
}

/* Smooth transitions */
.transition-all {
  transition-property: all;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 300ms;
}

/* Card hover effects */
.group:hover .group-hover\:shadow-xl {
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

/* Gradient background animation */
@keyframes gradient-shift {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

.bg-gradient-to-br {
  background-size: 200% 200%;
  animation: gradient-shift 15s ease infinite;
}
</style>