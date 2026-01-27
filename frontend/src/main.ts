import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
// import './assets/css/main.css'

// Font Awesome
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
    faArrowUp,
    faHome,
    faSignInAlt,
    faUserPlus,
    faUsersCog,
    faEye,
    faPlus,
    faEdit,
    faTrash,
    faList,
    faSearch,
    faCheckCircle,
    faTimes,
    faUser,
    faLock,
    faSignature,
    faEnvelope,
    faSave,
    faRedo,
    faUserEdit,
    faBolt,
    faTrashAlt,
    faCircle,
    faSquare,
    faUserTimes,
    faExclamationTriangle,
    faListCheck,
    faCheckSquare,
    faChevronUp,
} from '@fortawesome/free-solid-svg-icons'

library.add(faArrowUp, faHome, faSignInAlt, faUserPlus, faUsersCog, faEye, faPlus, faEdit, faTrash, faList, faSearch, faCheckCircle, faTimes, faUser, faLock, faSignature, faEnvelope, faSave, faRedo, faUserEdit, faBolt, faTrashAlt, faCircle, faSquare, faUserTimes, faExclamationTriangle, faListCheck, faCheckSquare, faChevronUp)

const app = createApp(App)
app.component('font-awesome-icon', FontAwesomeIcon)
app.use(router as any)
app.mount('#app')