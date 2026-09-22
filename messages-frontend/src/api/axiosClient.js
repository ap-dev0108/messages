import axios from "axios";

const axiosClient = axios.create({
    baseURL: import.meta.url.env.VITE_API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

export default axiosClient;