import './assets/main.css'
import PrimeVue from 'primevue/config';
import { createApp } from 'vue'
import App from './App.vue'
import Aura from '@primeuix/themes/aura';
import 'primeicons/primeicons.css';
import { createPinia } from 'pinia'
import router from './router'
import ConfirmationService from 'primevue/confirmationservice'
import ConfirmDialog from 'primevue/confirmdialog'
import ToastService from 'primevue/toastservice'
import Toast from 'primevue/toast'



const app = createApp(App);

app.use(ToastService)
app.component('Toast', Toast)
app.use(ConfirmationService)
app.component('ConfirmDialog', ConfirmDialog)
app.use(createPinia())

app.use(PrimeVue, {
    theme: {
        preset: Aura
    }
});
app.use(router)
app.mount('#app');
