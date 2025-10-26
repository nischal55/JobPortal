<template>
  <div class="flex h-screen">
    <!-- Sidebar -->
    <aside
      :class="[ 'bg-emerald-700 text-emerald-100 transition-width duration-300 flex flex-col', isOpen ? 'w-64' : 'w-16' ]"
    >
      <!-- Sidebar Header: Title + Toggle Button -->
      <div class="flex items-center justify-between h-14 border-b border-green-600 px-4">
        <div class="flex items-center gap-2">
          <span v-if="isOpen" class="text-lg font-semibold tracking-wide whitespace-nowrap">
            Admin Panel
          </span>
        </div>

        <button
          @click="toggleSidebar"
          class="p-2 focus:outline-none hover:bg-green-600 rounded transition-colors"
          :aria-label="isOpen ? 'Collapse sidebar' : 'Expand sidebar'"
        >
          <i :class="['pi', isOpen ? 'pi-times' : 'pi-bars']" class="text-lg"></i>
        </button>
      </div>

      <!-- Menu Items -->
      <nav class="flex-1 mt-4 flex flex-col space-y-1">
        <a href="/dashboard" class="flex items-center gap-3 px-4 py-3 rounded-md transition-colors bg-green-600 text-green-100 font-semibold">
          <i class="pi pi-home text-lg"></i>
          <span v-if="isOpen" class="whitespace-nowrap">Dashboard</span>
        </a>
        <a href="jobProviderList" v-if="userRole === 'admin'" class="flex items-center gap-3 px-4 py-3 rounded-md transition-colors text-green-200 hover:bg-green-600 hover:text-green-100">
          <i class="pi pi-address-book text-lg"></i>
          <span v-if="isOpen" class="whitespace-nowrap">Job Providers</span>
        </a>
         <a href="jobseeker" v-if="userRole==='admin'" class="flex items-center gap-3 px-4 py-3 rounded-md transition-colors text-green-200 hover:bg-green-600 hover:text-green-100">
          <i class="pi pi-users text-lg"></i>
          <span v-if="isOpen" class="whitespace-nowrap">Job Seekers</span>
        </a>
        <a href="/jobList" class="flex items-center gap-3 px-4 py-3 rounded-md transition-colors text-green-200 hover:bg-green-600 hover:text-green-100">
          <i class="pi pi-briefcase text-lg"></i>
          <span v-if="isOpen" class="whitespace-nowrap">Posted Jobs</span>
        </a>
        <a href="/adminSupport" class="flex items-center gap-3 px-4 py-3 rounded-md transition-colors text-green-200 hover:bg-green-600 hover:text-green-100">
          <i class="pi pi-cog text-lg"></i>
          <span v-if="isOpen" class="whitespace-nowrap">Admin Support</span>
        </a>
      </nav>
    </aside>


  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

const isOpen = ref(true)
const isProfileOpen = ref(false)

function toggleSidebar() {
  isOpen.value = !isOpen.value
}

function toggleProfileDropdown() {
  isProfileOpen.value = !isProfileOpen.value
}

function onClickOutside(event) {
  const dropdown = document.getElementById('profile-dropdown')
  const button = document.getElementById('profile-button')

  if (
    dropdown &&
    button &&
    !dropdown.contains(event.target) &&
    !button.contains(event.target)
  ) {
    isProfileOpen.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', onClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', onClickOutside)
})


const userRole = ref(null)

onMounted(() => {
  userRole.value = localStorage.getItem('user_role')
})


</script>

<style scoped>
aside {
  transition-property: width;
  transition-duration: 300ms;
}
</style>
