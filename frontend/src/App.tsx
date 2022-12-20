
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

import styles from "./styles/App.module.css";
import EmailCheckOK from "./routes/EmailCheckOK";
function App() {
    return (
        <div className="App">
            <Nav />
            <div className={styles.content}>
            <Routes>
                <Route path='*' element={<NotFound />}/>
                <Route path="/" element={<Home/>} />
                <Route path="/mulmultrade" element={ <MulmulTrade/>}/>
                <Route path="/mulmultrade/mulmultrade2" element={<MulmulTrade2/>} />
                <Route path="/login" element={<Login/>}/>
                <Route path="/signup" element={<SignUp/>}/>
                <Route path="/kirikiritrade" element={<KiriKiriTrade/>} />
                <Route path="/mypage" element={<MyPage/>} />
                <Route path="/mypage/zzim" element={<Zzim/>} />
                <Route path="/signup/emailcheck" element={<EmailCheck/>}/>
                <Route path="/signup/emailcheck/ok" element={<EmailCheckOK/>}/>
                <Route path="/postupload" element={<PostUpload/>}/>

            </Routes>
        </div>
        </div>
    );
}

export default App;
