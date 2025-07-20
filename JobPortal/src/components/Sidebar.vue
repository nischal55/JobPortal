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
        <a href="#" class="flex items-center gap-3 px-4 py-3 rounded-md transition-colors bg-green-600 text-green-100 font-semibold">
          <i class="pi pi-home text-lg"></i>
          <span v-if="isOpen" class="whitespace-nowrap">Dashboard</span>
        </a>
        <a href="#" class="flex items-center gap-3 px-4 py-3 rounded-md transition-colors text-green-200 hover:bg-green-600 hover:text-green-100">
          <i class="pi pi-address-book text-lg"></i>
          <span v-if="isOpen" class="whitespace-nowrap">Job Providers</span>
        </a>
         <a href="#" class="flex items-center gap-3 px-4 py-3 rounded-md transition-colors text-green-200 hover:bg-green-600 hover:text-green-100">
          <i class="pi pi-users text-lg"></i>
          <span v-if="isOpen" class="whitespace-nowrap">Job Seekers</span>
        </a>
        <a href="#" class="flex items-center gap-3 px-4 py-3 rounded-md transition-colors text-green-200 hover:bg-green-600 hover:text-green-100">
          <i class="pi pi-briefcase text-lg"></i>
          <span v-if="isOpen" class="whitespace-nowrap">Jobs Available</span>
        </a>
          <a href="#" class="flex items-center gap-3 px-4 py-3 rounded-md transition-colors text-green-200 hover:bg-green-600 hover:text-green-100">
          <i class="pi pi-list-check text-lg"></i>
          <span v-if="isOpen" class="whitespace-nowrap">Jobs Applicants</span>
        </a>
        <a href="#" class="flex items-center gap-3 px-4 py-3 rounded-md transition-colors text-green-200 hover:bg-green-600 hover:text-green-100">
          <i class="pi pi-cog text-lg"></i>
          <span v-if="isOpen" class="whitespace-nowrap">Settings</span>
        </a>
      </nav>
    </aside>

    <!-- Main content layout -->
    <div class="flex-1 flex flex-col">
      <!-- Navbar -->
      <nav class="h-14 bg-white dark:bg-gray-900 shadow flex items-center justify-between px-6">
        <h1 class="text-xl font-semibold text-gray-800 dark:text-gray-100">Dashboard</h1>

        <!-- Profile dropdown -->
        <div class="relative">
          <button
            id="profile-button"
            @click="toggleProfileDropdown"
            class="flex items-center gap-2 focus:outline-none cursor-pointer"
            aria-haspopup="true"
            :aria-expanded="isProfileOpen.toString()"
          >
            <img
              src="https://i.pravatar.cc/40"
              alt="Profile"
              class="w-8 h-8 rounded-full border-2 border-emerald-600"
            />
            <span class="hidden sm:inline text-gray-700 dark:text-gray-200 font-medium">
              Admin User
            </span>
            <i :class="['pi', isProfileOpen ? 'pi-angle-up' : 'pi-angle-down']" class="text-gray-700 dark:text-gray-200"></i>
          </button>

          <!-- Dropdown -->
          <div
            v-show="isProfileOpen"
            id="profile-dropdown"
            class="absolute right-0 mt-2 w-48 bg-white dark:bg-gray-800 rounded-md shadow-lg py-2 z-50 cursor-pointer"
          >
            <a href="#" class="block px-4 py-2 text-gray-700 dark:text-gray-200 hover:bg-emerald-600 hover:text-white">Profile</a>
            <a href="#" class="block px-4 py-2 text-gray-700 dark:text-gray-200 hover:bg-emerald-600 hover:text-white">Settings</a>
            <a href="#" class="block px-4 py-2 text-gray-700 dark:text-gray-200 hover:bg-emerald-600 hover:text-white">Logout</a>
          </div>
        </div>
      </nav>

      <!-- Slot for page content -->
      <main class="flex-1 overflow-auto bg-surface-50 dark:bg-surface-900 py-6 px-1">
        <slot />
      </main>
    </div>
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
</script>

<style scoped>
aside {
  transition-property: width;
  transition-duration: 300ms;
}
</style>
