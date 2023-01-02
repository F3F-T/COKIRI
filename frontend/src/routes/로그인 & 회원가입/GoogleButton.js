import styles from '../../styles/loginAndSignup/googleLoginModal.module.css'
import React, {useCallback, useEffect, useState} from "react";
import {gapi} from "gapi-script";
import {GoogleLogin} from "@react-oauth/google";
import {GoogleOAuthProvider} from "@react-oauth/google";
import jwt_decode from 'jwt-decode';



const GoogleButton =  () =>{
    //일단 여기다가 유저 정보 담아
    const [user,setUser] = useState({});
    function  handleCallbackResponse(response){
        console.log("아이디 토큰" + response.credential);
        var userObject = jwt_decode(response.credential);
        console.log(userObject);
        setUser(userObject);
        document.getElementById("signInDiv").hidden =  true;
    }

    function handleSignOut(event){
        setUser({});
        document.getElementById("signInDiv").hidden =  false ;
    }
    useEffect(()=>{
        /* global google */
        google.accounts.id.initialize({
            client_id:"502345601007-gv64iag1rq1un755oo06q126ghmfgkqk.apps.googleusercontent.com",
            callback: handleCallbackResponse
        });
        google.accounts.id.renderButton(
            document.getElementById("signInDiv"),
            {theme:"outline",size:"large"}
        )
    },[])
    //로그인 안된 상태에서는 로그인 버튼

    //로그인 된 상태면 로그아웃 버튼

    return(
        <>
        <div id ="signInDiv"></div>
            <button onClick={(e)=>handleSignOut(e)}>Sign Out</button>
            {user &&
                <div>
                    <img src={user.picture}/>
                    <p>{user.name}</p>
                </div>
            }
        </>
    )
};

export default GoogleButton;