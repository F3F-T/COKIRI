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
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../index";
import {reject} from "list";
import {resetCategory} from "../../store/categoryReducer";
import {setUserAddressInfo1,setAddressName1,resetaddress1} from "../../store/userAddressInfoReducer";
import {setUserAddressInfo2,setAddressName2,resetaddress2} from "../../store/userAddressInfoReducer";
interface AddressType {
    userId:Number;
    addressName:string;
    postalAddress:string;//우편번호
    latitude:string;
    longitude:string;
}
const NeighborAuth = () => {
    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const location = useGeoLocation();
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const addressR = useSelector((state : Rootstate)=>{return state.userAddressInfoReducer})
    const [addressInfo, setAddressInfo] = useState<AddressType>(null);
    const [addressID,setAddressID]=useState('')
    const [parcel_1,setParcel_1] = useState('');
    const [parcel_2,setParcel_2] = useState('');

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
    const getAddressBtn = () => {
        getAddressData(addressID)
    }
    //주소 추가
    async function postAddressData_1() {
        try{
            const res = await Api.post('/address',addressInfo);
            console.log("첫번째 위치정보", addressInfo);
            setAddressID(res.data.id);
            dispatch(setUserAddressInfo1((res.data.id)))
            dispatch(setAddressName1((res.data.addressName)))
            alert("추가 성공")
        }
        catch (err)
        {
            console.log(err)
            alert("추가 실패")
        }
    }
    async function postAddressData_2() {
        try{
            const res = await Api.post('/address',addressInfo);
            console.log(res)
            console.log("두번째 위치정보", addressInfo);
            setAddressID(res.data.id);
            dispatch(setUserAddressInfo2((res.data.id)))
            dispatch(setAddressName2((res.data.addressName)))
            alert("추가 성공")
        }
        catch (err)
        {
            console.log(err)
            alert("추가 실패")
        }
    }
    //주소 delete
    async function deleteAddress_1() {
        try{
            const addressDelete1={
                data: {
                    userId: info.id,
                    addressId: addressR.addressId1
                }
            }
            const res = await Api.delete('/address', addressDelete1);
            dispatch(resetaddress1())
            alert("삭제 성공")
        }
        catch (err)
        {
            console.log(err)
            alert("삭제 실패")
        }
    }
    async function deleteAddress_2() {
        try{
            const addressDelete2={
                data: {
                    userId: info.id,
                    addressId: addressR.addressId2
                }
            }
            const res = await Api.delete('/address', addressDelete2);
            dispatch(resetaddress2())
            alert("삭제 성공")
        }
        catch (err)
        {
            console.log(err)
            alert("삭제 실패")
        }
    }
    const inputAddressName_1 = (e) => {
        let inputName = e.target.value;
        if (inputName.length > 0) {
            getAddr_1(JSON.stringify(location.coordinates.lat),JSON.stringify(location.coordinates.lng))
            setAddressInfo((prevState) => {
                return {
                    ...prevState,
                    userId: info.id,
                    addressName: e.target.value,
                    postalAddress: parcel_1,
                    latitude: JSON.stringify(location.coordinates.lat),
                    longitude: JSON.stringify(location.coordinates.lng),
                }
            })
        }
    }
    const inputAddressName_2= (e) => {
        console.log("대답")
        let inputName = e.target.value;
        if (inputName.length > 0) {
            getAddr_2(JSON.stringify(location.coordinates.lat),JSON.stringify(location.coordinates.lng))
            setAddressInfo((prevState) => {
                return {
                    ...prevState,
                    userId: info.id,
                    addressName: e.target.value,
                    postalAddress: parcel_2,
                    latitude: JSON.stringify(location.coordinates.lat),
                    longitude: JSON.stringify(location.coordinates.lng),
                }
            })
        }
        else{

        }
    }
    /*global kakao*/
    function getAddr_1(lat,lng) {
        // @ts-ignore
        let geocoder = new kakao.maps.services.Geocoder();
        // @ts-ignore
        let coord = new kakao.maps.LatLng(lat, lng);
        let callback = function (result, status) {
            // @ts-ignore
            if (status === kakao.maps.services.Status.OK) {
                const arr = {...result};
                const _arr = arr[0].address.address_name;
                console.log("kakao주소1", _arr)
                setParcel_1(_arr);
            }
        }
        geocoder.coord2Address(coord.getLng(), coord.getLat(), callback);
    }
    function getAddr_2(lat,lng){
        // @ts-ignore
        let geocoder = new kakao.maps.services.Geocoder();
        // @ts-ignore
        let coord = new kakao.maps.LatLng(lat, lng);
        let callback = function(result, status) {
            // @ts-ignore
            if (status === kakao.maps.services.Status.OK) {
                const arr  ={ ...result};
                const _arr = arr[0].address.address_name;
                console.log("kakao주소2", _arr)
                setParcel_2(_arr);
            }
        }
        geocoder.coord2Address(coord.getLng(), coord.getLat(), callback);
    }
    return (
        <>
            <div className={styles.box}>
                <div className={styles.gps1}>
                    <button className={styles.gps1Btn} onClick={postAddressData_1}>+</button>
                    <button className={styles.gps1Btn} onClick={getAddressBtn}>조회</button>
                    <button className={styles.gps1Btn} onClick={deleteAddress_1}>delete</button>
                    <TextInput placeholder={"첫번째 주소를 입력해주세요"} onChange={inputAddressName_1}/>
                    <div className={styles.gps1_1}>{addressR.addressName1}</div>
                    <div className={styles.gps1_1}></div>
                </div>
                <div className={styles.gps1}>
                    <button className={styles.gps1Btn} onClick={postAddressData_2}>+</button>
                    <button className={styles.gps1Btn} onClick={getAddressBtn}>조회</button>
                    <button className={styles.gps1Btn} onClick={deleteAddress_2}>delete</button>
                    <TextInput placeholder={"두번째 주소를 입력해주세요"} onChange={inputAddressName_2}/>
                    <div className={styles.gps1_1}>{addressR.addressName2}</div>
                    <div className={styles.gps1_1}></div>
                </div>
            </div>
        </>
    );
}

export default NeighborAuth;