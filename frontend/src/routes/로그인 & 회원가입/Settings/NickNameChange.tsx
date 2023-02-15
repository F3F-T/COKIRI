import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../../../styles/loginAndSignup/SettingRight.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import photo from "../img/photoSelect.png"
import TextInput from "../../../component/common/TextInput";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../../index";
import Api from "../../../utils/api";
import {setUserNick} from "../../../store/userInfoReducer";
import axios from "axios";
import Message from "../../../component/로그인 & 회원가입/Message";


const NickNameChange = () =>  {

    interface UserInfo {
        userId:number;
        newNickname: string;

    }
    type checkNicknameTypes = 'invalid' | 'valid' | 'duplicated'
    interface ValidationCheck {
        nicknameCheck: checkNicknameTypes;
        nicknameCheckBoolean: boolean;
    }
    const dispatch = useDispatch();
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const [userInfo, setuserInfo] = useState<UserInfo>(null);
    const [validationCheck, setValidationCheck] = useState<ValidationCheck>(
        {
            nicknameCheck: undefined,
            nicknameCheckBoolean: undefined,
        }
    );
    useEffect(()=>{
        readNickName()
    },[])
    const [newNick,setNewNick]=useState(info.nickname)
    if (! readNickName) {
        return null
    }
    if (! nicknameChange) {
        return null
    }
    const onChangeNickname = (e) => {
        let inputNickname = e.target.value;
        //이메일 유효성 검사를 통과했을때, (형식에 맞는 경우 true 리턴)
        if (inputNickname.length > 0) {
            //닉네임 중복체크 백엔드 통신
            //string인 inputNickname을 json형태의 객체로 변환
            let jsonObj = {"nickname": inputNickname};
            //변환한 json 객체로 이메일 중복체크
            CheckNickNameDuplicated(jsonObj);
        } else //닉네임 유효성 검사 실패했을때
        {
            setValidationCheck((prevState) => {
                return {...prevState, nicknameCheck: "invalid", nicknameCheckBoolean: false}
            })
        }
    }

    async function CheckNickNameDuplicated(nickname: object) {

        try {
            const res = await axios.post("http://localhost:8080/auth/check-nickname", nickname);
            const result = res.data;
            const duplicated = result.exists

            if (duplicated) //중복인 경우 -> true 반환
            {
                setValidationCheck((prevState) => {
                    return {...prevState, nicknameCheck: "duplicated", nicknameCheckBoolean: false}
                })
            } else //중복이 아닌 경우 -> false 반환
            {
                setValidationCheck((prevState) => {
                    return {...prevState, nicknameCheck: "valid", nicknameCheckBoolean: true}
                })
                setuserInfo((prevState) => {
                    return {
                        ...prevState, newNickname: nickname["nickname"]
                    }
                })
                // dispatch(setUserNick(nickname["nickname"]))

            }

        } catch (err) {
            console.log(err);
            alert('서버와 통신 실패');

        }
    }

    async function nicknameChange() {
        try {
            const userInfo1={
                userId: userInfo.userId,
                newNickname: userInfo.newNickname
            }

            const res = await Api.patch("/user/nickname", userInfo1);
            setNewNick(res.data.newNickname)
            dispatch(setUserNick(res.data.newNickname))
            alert('닉넴 변경 성공');
        } catch (err) {
            console.log(err);
            alert('닉넴 변경 실패');

        }
    }
    async function readNickName(){
        try{
            const res = await  Api.get("/user")
            console.log("유저정보",res.data.id)
            setuserInfo((prevState) => {
                return {
                    ...prevState, userId: res.data.userDetail.id
                }
            })
        }
        catch (err){
            console.log(err);
            alert("실패??")
        }
    }
    return (
        <div className={styles.box1}>
            <div className={styles.ad1}>닉네임 변경하기</div>
            <div className={styles.ad2}>닉네임을 입력하세요.(최대 6글자까지 입력이 가능해요.)</div>
            <div className={styles.inputNickBox}>
            <input className={styles.inputNick} placeholder={info.nickname} onChange={onChangeNickname}/>
            <button className={styles.nickChangeBtn} onClick={nicknameChange}>변경</button>
            </div>
            {(validationCheck.nicknameCheck === undefined &&
                    <div className={styles.valCheck}>
                        <Message validCheck={validationCheck.nicknameCheckBoolean} content={""}/>
                    </div>
                )
                ||
                (validationCheck.nicknameCheck === "valid" &&
                    <div className={styles.valCheck}>
                        <Message validCheck={validationCheck.nicknameCheckBoolean} content={"✔ 사용 가능한 닉네임입니다."}/>
                    </div>)
                ||
                (validationCheck.nicknameCheck === "invalid" &&
                    <div className={styles.valCheck}>
                        <Message validCheck={validationCheck.nicknameCheckBoolean} content={"❌ 닉네임은 한글자 이상이어야합니다."}/>
                    </div>)
                ||
                (validationCheck.nicknameCheck === "duplicated" &&
                    <div className={styles.valCheck}>
                        <Message validCheck={validationCheck.nicknameCheckBoolean} content={"❌ 이미 가입된 닉네임입니다."}/>
                    </div>)}
        </div>
    );
}

export default NickNameChange;