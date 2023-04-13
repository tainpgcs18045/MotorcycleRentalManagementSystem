import { initializeApp } from "firebase/app";
import { getStorage } from "firebase/storage";

const firebaseConfig = {
    apiKey: "AIzaSyA2LdXcdlrW-VRotrds4fDd20F-4FpEWzw",
    authDomain: "bike-hiring-management-d7a01.firebaseapp.com",
    projectId: "bike-hiring-management-d7a01",
    storageBucket: "bike-hiring-management-d7a01.appspot.com",
    messagingSenderId: "353723223902",
    appId: "1:353723223902:web:a184b83eb63a34e3b749cf",
    measurementId: "G-FFKHGSPLVH"
};

const app = initializeApp(firebaseConfig);
export const storage = getStorage(app);