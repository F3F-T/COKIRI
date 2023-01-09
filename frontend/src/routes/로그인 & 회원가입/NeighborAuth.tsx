import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/loginAndSignup/neighborAuth.module.css";
import loginImg from "../../img/cokkiriLogo.png";
import {useNavigate, useLocation} from "react-router-dom";
import Button from "../../component/common/Button";
import axios from "axios";
import TextInput from "../../component/common/TextInput";
import Message from "../../component/로그인 & 회원가입/Message";
import classNames from "classnames/bind";
import Api from "../../utils/api";
import {setToken} from "../../store/jwtTokenReducer";
import {setUserInfo} from "../../store/userInfoReducer";
import useGeoLocation from "../../hooks/useGeolocation";
import {useSelector} from "react-redux";
import {Rootstate} from "../../index";
interface AddressType {
// {"userId":5, "addressName":"회사","postalAddress":"999","latitude":"37.49455","longitude":"127.12170"}
    userId:Number;
    addressName:string;
    postalAddress:string;//우편번호
    latitude:string;
    longitude:string;
}
interface AddressDeleteType {
    userId:Number;
    addressId:Number;
}
const NeighborAuth = () => {
    const location = useGeoLocation();
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const [addressInfo, setAddressInfo] = useState<AddressType>(null);
    const [addressDelete, setAddressDeleteInfo] = useState<AddressDeleteType>(null);

    //위치 추가
    async function postAddressData(addressInfo:object) {
        try{
            const res = await Api.post('/address',addressInfo);
            console.log(res)
            console.log("위치정보 추가2", addressInfo);
            alert("추가 성공")

        }
        catch (err)
        {
            console.log(err)
            alert("추가 실패")
        }
    }
    function addressCheck(){
        setAddressInfo((prevState) => {
            return {...prevState,
                userId: info.id,
                addressName: "wd",
                postalAddress:"99",
                latitude: JSON.stringify(location.coordinates.lat) ,
                longitude: JSON.stringify(location.coordinates.lng),
            }
        })
        console.log("위치정보 추가", addressInfo);
    }

    const addressPostBtn = () => {
        postAddressData(addressInfo)
    }
    // useEffect(()=>{
    //     postAddressData();
    // },[])
    //주소 update

    //주소 delete
    async function deleteAddress(addressDelete:object) {
        try{
            const res = await Api.delete('/address',addressDelete);
            console.log(res)
            console.log("위치정보삭제", addressDelete);
            alert("삭제 성공")

        }
        catch (err)
        {
            console.log(err)
            alert("삭제 실패")
        }
    }
    const deleteAddressBtn = () => {
        deleteAddress(addressDelete)
    }



    return (
        <>
            <div className={styles.box}>
                <div className={styles.gps1}>
                    <button className={styles.gps1Btn}onClick={addressPostBtn}>+</button>
                    <button className={styles.gps1Btn}>update</button>
                    <button className={styles.gps1Btn} onClick={deleteAddressBtn}>delete</button>
                    <div className={styles.gps1_1}></div>
                    <div className={styles.gps1_1}></div>

                </div>
                <div className={styles.gps1}>
                    <button className={styles.gps1Btn} onClick={addressCheck}>+</button>
                    <button className={styles.gps1Btn}>update</button>
                    <button className={styles.gps1Btn}>delete</button>
                    <div className={styles.gps1_1}></div>
                    <div className={styles.gps1_1}></div>
                </div>
            </div>
        </>
    );
}

export default NeighborAuth;