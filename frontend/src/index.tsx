import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
// import {Router} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import {configureStore,combineReducers} from "@reduxjs/toolkit";
import {Provider} from "react-redux";
import categoryReducer from "./store/categoryReducer";
import postDetailReducer from "./store/postDetailReducer";
import {GoogleOAuthProvider} from "@react-oauth/google";
import jwtTokenReducer from "./store/jwtTokenReducer";
import userInfoReducer from "./store/userInfoReducer";
//interceptor에서 redux store 사용을 위한 injection
import {injectStore} from "./utils/api";
//interceptor에서 navigate 사용을 위함
import {createBrowserHistory} from "history";
import {unstable_HistoryRouter as HistoryRouter} from "react-router-dom";
import storage from 'redux-persist/lib/storage';
import {
    persistStore,
        persistReducer,
        FLUSH,
        REHYDRATE,
        PAUSE,
        PERSIST,
        PURGE,
        REGISTER,
} from 'redux-persist';
import {PersistGate} from "redux-persist/integration/react";
import userAddressInfoReducer from "./store/userAddressInfoReducer";
import RefreshReducer from "./store/refreshReducer";
import PostQueryStringReducer from "./store/priceReducer";
import PriceReducer from "./store/priceReducer";
import talkCardReducer from "./store/talkCardReducer";

/**
 * configureStore을 import해온다.
 * store을 configureStore을 통해서 생성, reducer에는 만들 reducer을 객체형태로 저장한다.
 * 객체의 값은 리듀서가 들어간다.
 * counterReducer는 counterReducer.ts의 counterSlice.reducer이다 !!
 * 이것을 Provider로 <App/>을 감싸면 App내의 모든 컴포넌트는 특정 훅을 통해 store의 내용을 자유롭게 사용할수 있다.
 */


//store/counterReducer에서 만든 reducer, action, state, 초기 설정값등을 reducer에 묶어주고, counter로 선언해준다


const persistConfig = {
    key: 'root',
    storage,
    //여기서 영구적으로 저장하고 싶지 않은 reducer를 blackList로 설정해준다.
    // (정렬기준에서 가격은 새로고침 되면 없어지게 하고싶으므로 priceReducer를 추가해주었음)
    blacklist : ['priceReducer']
};

const rootReducer = combineReducers({
    categoryReducer: categoryReducer,
    postDetailReducer: postDetailReducer,
    jwtTokenReducer: jwtTokenReducer,
    userInfoReducer: userInfoReducer,
    userAddressInfoReducer: userAddressInfoReducer,
    refreshReducer : RefreshReducer,
    priceReducer : PriceReducer,
    talkCardReducer : talkCardReducer
});

const persistedReducer = persistReducer(persistConfig,rootReducer);


export const store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: {
                ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
            },
            // }).concat(logger),
        }),
})

export const persistor = persistStore(store);

injectStore(store);


export type Rootstate = ReturnType<typeof store.getState>
// @ts-ignore
export const history = createBrowserHistory();

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <GoogleOAuthProvider clientId={'502345601007-gv64iag1rq1un755oo06q126ghmfgkqk.apps.googleusercontent.com'}>
        {/*@ts-ignore*/}
        <HistoryRouter history={history}>

            <Provider store={store}>
                <PersistGate loading={null} persistor={persistor}>
                    <App/>
                </PersistGate>
            </Provider>
        </HistoryRouter>

    </GoogleOAuthProvider>




);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
