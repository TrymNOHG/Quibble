import { useUserStore } from "@/stores/counter.js";
import router from "@/router/index.js";
import {getNewAccessToken} from "@/services/UserService.js";

let mockToken = null;

export function setMockToken(token) {
    mockToken = token;
}

export default async function sessionToken() {
    if (mockToken) {
        return mockToken;
    }


    const sessionToken = useUserStore().getToken;
    if (sessionToken === null) {
        await router.push("/login");
        throw new Error("Session token cannot be null. Login in again.");
    }

    //if sesstion token is expired, get a new one
    if (useUserStore().tokenExpired) {
        const token = await getNewAccessToken(sessionToken);
        useUserStore().setToken(token.data.token);
        useUserStore().setTokenExpires(token.data.token_expiration);

        if (token.data.accessToken === null) {
            useUserStore().logoutUser();
            await router.push("/login");
            throw new Error("Session token expired. Login in again.");
        }
    }



    return sessionToken;
}