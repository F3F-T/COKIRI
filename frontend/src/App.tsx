
import React from "react";

import Home from "./routes/Home";
import { Route, Routes } from "react-router-dom";
import MyPage from "./routes/MyPage";
import MulmulTrade from "./routes/MulmulTrade";
import MulmulTrade2 from "./routes/MulmulTrade2";
import KiriKiriTrade from "./routes/KiriKiriTrade";
import Nav from "./component/Nav";
import { Navigate } from "react-router-dom";
import NotFound from "./component/NotFound";
import Login from "./routes/Login";
import SignUp from "./routes/SignUp";
import EmailCheck from "./routes/EmailCheck";
import PostUpload from "./routes/PostUpload";
import Zzim from "./routes/Zzim";

import styles from "./styles/App.module.css";
import EmailCheckOK from "./routes/EmailCheckOK";
import PostDetail from "./routes/PostDetail";
import MulmulTrade1 from "./routes/MulMulTrade1";
function App() {
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

                <Route path="/post" element={<PostDetail/>}/>
                <Route path="/postupload" element={<PostUpload/>}/>

                <Route path='*' element={<NotFound />}/>
            </Routes>

        </div>
        </div>
    );
}

export default App;
