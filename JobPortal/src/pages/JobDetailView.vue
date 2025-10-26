<template>
  <div class="min-h-screen bg-gray-50">
    <navbar />

    <!-- Confirmation Dialog -->
    <ConfirmDialog />
    <!-- Toast Notifications -->
    <Toast />

    <div class="pt-[70px] px-6 py-10 max-w-7xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-8">
      <!-- Main Job Details -->
      <div class="lg:col-span-2 bg-white shadow rounded-2xl p-8">
        <h2 class="text-3xl font-bold text-gray-800 mb-6">{{ job.title }}</h2>

        <div class="space-y-3 text-gray-700 mb-6">
          <div><span class="font-semibold">Job Type:</span> {{ job.type }}</div>
          <div><span class="font-semibold">Location:</span> {{ job.location }}</div>
          <div><span class="font-semibold">Salary Range:</span> {{ job.salaryRange }}</div>
          <div><span class="font-semibold">Deadline:</span> {{ formatDate(job.deadline) }}</div>
          <div><span class="font-semibold">Posted On:</span> {{ formatDateTime(job.createdAt) }}</div>
        </div>

        <div class="mb-6">
          <h3 class="text-xl font-semibold mb-2">Job Description</h3>
          <div class="prose max-w-none break-words" v-html="job.description" />
        </div>

        <div>
          <h3 class="text-xl font-semibold mb-2">Requirements</h3>
          <div class="prose max-w-none break-words" v-html="job.requirements" />
        </div>

        <!-- Apply Button -->
        <div class="mt-6">
          <Button 
            label="Apply to Job" 
            class="w-full p-button-lg" 
            @click="confirmApply" 
          />
        </div>
      </div>

      <!-- Sidebar - Related Jobs -->
      <div class="bg-white shadow rounded-2xl p-6">
        <h3 class="text-xl font-semibold mb-4">Related Jobs</h3>

        <div v-if="relatedJobs.length > 0" class="space-y-4">
          <div 
            v-for="related in relatedJobs" 
            :key="related.id"
            class="border-b pb-3 last:border-b-0 shadow-md p-5 rounded"
          >
            <router-link 
              :to="`/jobDetail/${related.id}`" 
              class="text-emerald-600 font-medium hover:underline"
            >
              {{ related.title }}
            </router-link>
            <div class="text-sm text-gray-500">{{ related.location }} • {{ related.type }}</div>
          </div>
        </div>

        <div v-else class="text-gray-500 italic">
          No related jobs found.
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ApiService from '@/services/ApiService'
import navbar from '@/components/navbar.vue'
import Button from 'primevue/button'
import { useConfirm } from 'primevue/useconfirm'
import { useToast } from 'primevue/usetoast'

const confirm = useConfirm()
const toast = useToast()
const route = useRoute()
const job = ref({})
const relatedJobs = ref([])

const seekerId = localStorage.getItem('jobseeker_id')
const resumeId = localStorage.getItem('resume_detail_id')
const userId = localStorage.getItem("user_id")
const router = useRouter();

onMounted(() => {
  fetchJobDetails()
})

watch(() => route.params.id, () => {
  fetchJobDetails()
})

function fetchJobDetails() {
  const jobId = route.params.id
  ApiService.get(`http://localhost:8888/jobDetail/findById/${jobId}`)
    .then(res => {
      job.value = res.data
      fetchRelatedJobs(res.data)
    })
    .catch(err => console.error('Error fetching job detail:', err))
}

function fetchRelatedJobs(currentJob) {
  ApiService.get(`http://localhost:8888/jobDetail/findAll`)
    .then(res => {
      relatedJobs.value = res.data
        .filter(j => j.id !== currentJob.id && j.type === currentJob.type)
        .slice(0, 5)
    })
    .catch(err => console.error('Error fetching related jobs:', err))
}

function confirmApply() {
  confirm.require({
    message: `Are you sure you want to apply for "${job.value.title}"?`,
    header: 'Confirm Application',
    icon: 'pi pi-exclamation-triangle',
    acceptClass: 'p-button-success',
    acceptLabel: 'Yes, Apply',
    rejectLabel: 'Cancel',
    accept: () => {
      if(userId == null){
        router.push('/userLogin')
      }else{
        applyToJob()
      }
    }
  })
}

function applyToJob() {
  const payload = {
    job: { id: job.value.id },
    seeker: { id: seekerId },
    resume: { id: resumeId },
    coverLetter: "I am very interested in this position.",
    status: "applied"
  }

  ApiService.post(`http://localhost:8888/jobApplicants/create`, payload)
    .then(() => {
      toast.add({
        severity: 'success',
        summary: 'Application Submitted',
        detail: `You have successfully applied for "${job.value.title}".`,
        life: 3000
      })
    })
    .catch(err => {
      console.error('Error applying to job:', err)
      toast.add({
        severity: 'error',
        summary: 'Application Failed',
        detail: err.response.data,
        life: 3000
      })
    })
}

const formatDate = date => new Date(date).toLocaleDateString()
const formatDateTime = date => new Date(date).toLocaleString()
</script>
