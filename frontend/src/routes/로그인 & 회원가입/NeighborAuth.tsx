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
import {reject} from "list";


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
    addressId:string;
}
const NeighborAuth = () => {
    const location = useGeoLocation();
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const [addressInfo, setAddressInfo] = useState<AddressType>(null);
    const [addressDelete, setAddressDeleteInfo] = useState<AddressDeleteType>(null);
    const [addressID,setAddressID]=useState('')
    // `${addressInfo.userId}`
    //주소 조회
    async function getAddressData(addressID) {
        try{
            const res = await Api.get('/address/'+`${addressID}`);
            console.log("위치정보 조회", res.data);
            alert("조회 성공")

        }
        catch (err)
        {
            console.log(err)
            alert("조회 실패")
        }
    }


    //주소 추가
    async function postAddressData() {
        try{
            // addressCheck()
            const addressInfo2 = {
                userId: info.id,
                addressName: "회사",
                postalAddress: "99",
                latitude: JSON.stringify(location.coordinates.lat),
                longitude: JSON.stringify(location.coordinates.lng),
            }
            const res = await Api.post('/address',addressInfo2);
            console.log(res)
            console.log("위치정보 추가2", addressInfo2);
            setAddressID(res.data.id);
            console.log("위치정보ID", res.data.id);
            setAddressDeleteInfo((prevState) => {
                return {...prevState,
                    userId: info.id,
                    addressId: addressID
                }
            })
            console.log("위치삭제정보 추가", addressDelete);
            alert("추가 성공")
        }
        catch (err)
        {
            console.log(err)
            alert("추가 실패")
        }
    }
    function addressCheck() {

            setAddressInfo((prevState) => {
               return {
                   ...prevState,
                   userId: info.id,
                   addressName: "회사",
                   postalAddress: "99",
                   latitude: JSON.stringify(location.coordinates.lat),
                   longitude: JSON.stringify(location.coordinates.lng),
               }
           })
           return addressInfo

       }

    async function postGPS(){
        await addressCheck();
        return addressInfo
    }



    // const addressPostBtn = () => {
    //     const promise = new Promise((resolve, reject) => {
    //         resolve(addressCheck());
    //     })
    //     promise.then(value => {
    //         console.log("sdfsdfds", value)
    //         postAddressData();
    //     })
    // }
    const addressPostBtn = () => {
        postAddressData();
    }


        // useEffect(()=>{
        //     postAddressData();
        // },[])
    //주소 update

    //주소 delete
    async function deleteAddress(addressDelete:object) {
        try{
            const res = await Api.delete('/address', addressDelete);
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
        console.log(addressDelete)
        deleteAddress(addressDelete)
    }
    const getAddressBtn = () => {
        getAddressData(addressID)
    }




    return (
        <>
            <div className={styles.box}>
                <div className={styles.gps1}>
                    <button className={styles.gps1Btn} onClick={addressPostBtn}>+</button>
                    <button className={styles.gps1Btn} onClick={getAddressBtn}>조회</button>
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