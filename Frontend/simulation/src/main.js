import { createApp } from 'vue'
import App from './App.vue'
import PrimeVue from 'primevue/config';
import Button from 'primevue/button';
import 'primeicons/primeicons.css';
import InputText from 'primevue/inputtext';
import 'primevue/resources/themes/saga-orange/theme.css';
import InputNumber from 'primevue/inputnumber';



const app=createApp(App)
app.use(PrimeVue);
app.component('Button',Button);
app.component('InputNumber',InputNumber);
app.component('InputText',InputText);
app.mount('#app')