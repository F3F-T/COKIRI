import axios, {AxiosRequestConfig} from 'axios';
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../index";
import {setToken} from "../store/jwtTokenReducer";


const store = useSelector((state: Rootstate) => state);
const dispatch = useDispatch();

const Api = axios.create({
    baseURL: "http://localhost:8080",
});

//accessToken을 header에 넣어준다
Api.interceptors.request.use(
    (config) => {
        const store = useSelector((state: Rootstate) => state);
        const dispatch = useDispatch();
        // HTTP Authorization 요청 헤더에 jwt-token을 넣음
        // 서버측 미들웨어에서 이를 확인하고 검증한 후 해당 API에 요청함.
        const accessToken = store.jwtTokenReducer.accessToken;

        try {
            if (!accessToken) {
                config.headers["Authorization"] = null;
            }
            else if (config.headers && accessToken) {
                config.headers["Authorization"] = `Bearer ${accessToken}`;
            }

        }catch (err)
        {
            console.error('[_axios.interceptors.request] config : ' + err);
        }

        return config;
    },

    (error) => {
        // 요청 에러 직전 호출됩니다.
        return Promise.reject(error);
    }
);

Api.interceptors.response.use(
    function (response) {
        return response;
    },
    async function (err) {
        const originalConfig = err.config;
        const store = useSelector((state: Rootstate) => state);
        const dispatch = useDispatch();

        if (err.response && err.response.status === 401) {
            const accessToken = store.jwtTokenReducer.accessToken;
            const jsonObj = {"accessToken" : accessToken}


            try {
                const data = await axios.post("/auth/reissue", jsonObj);

                const jwtToken = data.data.tokenInfo;

                if (jwtToken) {
                    dispatch(setToken(jwtToken));
                    return await Api.request(originalConfig);
                }
            } catch (err) {
                console.log("토큰 갱신 에러");
            }
            return Promise.reject(err);
        }
        return Promise.reject(err);
    }
);

export default Api;