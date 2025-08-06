<template>
  <div class="flex h-screen bg-gray-50">
    <!-- Sidebar -->
    <Sidebar />

    <!-- Main Content -->
    <div class="flex-1 flex flex-col">
      <AdminNav />

      <div class="flex-1 flex justify-center items-start px-6 py-10 overflow-auto">
        <div class="w-full max-w-3xl bg-white shadow rounded-2xl p-8">
          <h2 class="text-2xl font-bold text-gray-800 mb-6">Add Job Posting</h2>

          <form @submit.prevent="submitForm" class="space-y-6">
            <!-- Job Title -->
            <div>
              <label for="title" class="block text-sm font-medium text-gray-700 mb-1">Job Title</label>
              <InputText v-model="job.title" id="title" placeholder="e.g. Software Engineer" class="w-full" />
            </div>

            <!-- Description -->
            <div>
              <label for="description" class="block text-sm font-medium text-gray-700 mb-1">Description</label>
              <Textarea
                v-model="job.description"
                id="description"
                rows="4"
                placeholder="Brief description of the role"
                class="w-full"
              />
            </div>

            <!-- Location -->
            <div>
              <label for="location" class="block text-sm font-medium text-gray-700 mb-1">Location</label>
              <InputText v-model="job.location" id="location" placeholder="e.g. Kathmandu, Nepal" class="w-full" />
            </div>

            <!-- Salary Range -->
            <div>
              <label for="salaryRange" class="block text-sm font-medium text-gray-700 mb-1">Salary Range</label>
              <InputText
                v-model="job.salaryRange"
                id="salaryRange"
                placeholder="e.g. 50000 - 80000 NPR"
                class="w-full"
              />
            </div>

            <!-- Job Type -->
            <div>
              <label for="type" class="block text-sm font-medium text-gray-700 mb-1">Job Type</label>
              <Dropdown
                v-model="job.type"
                :options="types"
                placeholder="Select job type"
                class="w-full"
              />
            </div>

            <!-- Requirements -->
            <div>
              <label for="requirements" class="block text-sm font-medium text-gray-700 mb-1">Requirements</label>
              <Textarea
                v-model="job.requirements"
                id="requirements"
                rows="3"
                placeholder="e.g. Bachelor's degree, 2 years experience"
                class="w-full"
              />
            </div>

            <!-- Deadline -->
            <div>
              <label for="deadline" class="block text-sm font-medium text-gray-700 mb-1">Deadline</label>
              <input
                type="date"
                id="deadline"
                v-model="job.deadline"
                class="w-full border border-gray-300 rounded px-3 py-2"
              />
            </div>

            <!-- Submit Button -->
            <div class="pt-4">
              <Button type="submit" label="Post Job" class="w-full" />
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import InputText from 'primevue/inputtext'
import Textarea from 'primevue/textarea'
import Dropdown from 'primevue/dropdown'
import Button from 'primevue/button'
import Sidebar from '@/components/Sidebar.vue'
import AdminNav from '@/components/AdminNav.vue'
import ApiService from '@/services/ApiService'
// import axios from 'axios' // Uncomment if using axios

const types = ['full_time', 'part_time', 'internship', 'contract']

const job = ref({
  title: '',
  description: '',
  location: '',
  salaryRange: '',
  type: null,
  requirements: '',
  deadline: '',
  provider: {
    id : localStorage.getItem("user_id")
  }
})

const submitForm = () => {
  ApiService.post('http://localhost:8888/jobDetail/create', job.value)
    .then(response => {
      console.log('Job posted successfully:', response.data)
    })
    .catch(error => {
      console.error('Error posting job:', error)
    })
  
}
</script>

