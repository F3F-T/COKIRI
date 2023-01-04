
import React, {useEffect} from "react";

import Home from "./routes/Home";
import { Route, Routes } from "react-router-dom";
import MyPage from "./routes/MyPage";
import MulmulTrade from "./routes/MulmulTrade";
import MulmulTrade2 from "./routes/MulmulTrade2";
import KiriKiriTrade from "./routes/KiriKiriTrade";
import Nav from "./component/Nav";
import { Navigate } from "react-router-dom";
import NotFound from "./component/NotFound";
import Login from "./routes/로그인 & 회원가입/Login";
import SignUp from "./routes/로그인 & 회원가입/SignUp";
import EmailCheck from "./routes/로그인 & 회원가입/EmailCheck";
import PostUpload from "./routes/게시글/PostUpload";
import Zzim from "./routes/Zzim";

import styles from "./styles/App.module.css";
import EmailCheckOK from "./routes/로그인 & 회원가입/EmailCheckOK";
import PostDetail from "./routes/게시글/PostDetail";
import MulmulTrade1 from "./routes/MulMulTrade1";
import KokiriTalk from "./routes/KokiriTalk";
import GoogleButton from "./routes/로그인 & 회원가입/GoogleButton.js";
import { GoogleOAuthProvider } from '@react-oauth/google'

import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "./index";

function App() {

    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();

    //로그인 상태 변경에 따라 rendering 해주기 위함
    useEffect(()=>{
        console.log("jwt 토큰 effect 바뀜")
        console.log(store)
        console.log(store.jwtTokenReducer.authenticated);

    },[store.jwtTokenReducer.authenticated])

    return (
        <div className="App">
            <Nav />
            <div className={styles.content}>
            <Routes>
                <Route path="/" element={<Home/>} />
                <Route path="/login" element={<Login/>}/>

                <Route path="/signup" element={<SignUp/>}/>
                <Route path="/signup/emailcheck" element={<EmailCheck/>}/>
                <Route path="/signup/emailcheck/ok" element={<EmailCheckOK/>}/>

                <Route path="/mulmultrade" element={ <MulmulTrade/>}>
                    <Route path="" element={<MulmulTrade1/>}/>
                    <Route path="mulmultrade2" element={<MulmulTrade2/>}/>
                </Route>

                <Route path="/kirikiritrade" element={<KiriKiriTrade/>} />

                <Route path="/mypage" element={<MyPage/>} />
                <Route path="/mypage/zzim" element={<Zzim/>} />

                <Route path="/kokiritalk" element={<KokiriTalk/>} />

                <Route path="/post" element={<PostDetail/>}/>
                <Route path="/upload" element={<PostUpload/>}/>

                <Route path='*' element={<NotFound />}/>
            </Routes>

        </div>
        </div>
    );
}

export default App;
