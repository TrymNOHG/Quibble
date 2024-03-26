import { createI18n } from 'vue-i18n'
import no from "./norwegian.json"
import en from "./english.json"


const i18n = createI18n({
    legacy: false,
    locale: "NO",
    messages: {
        EN: en,
        NO: no,
    },
});

export default i18n;