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
import {setUserAddressInfo1} from "../../store/userAddressInfoReducer";
import {setAddressName1} from "../../store/userAddressInfoReducer";
import {resetaddress1} from "../../store/userAddressInfoReducer";


interface AddressType {
// {"userId":5, "addressName":"회사","postalAddress":"999","latitude":"37.49455","longitude":"127.12170"}
    userId:Number;
    addressName:string;
    postalAddress:string;//우편번호
    latitude:string;
    longitude:string;
}
interface AddressDeleteType {
    userId: Number;
    addressId: number;
}
const NeighborAuth = () => {
    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const location = useGeoLocation();
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const addressR = useSelector((state : Rootstate)=>{return state.userAddressInfoReducer})
    const [addressInfo, setAddressInfo] = useState<AddressType>(null);
    const [addressDelete, setAddressDeleteInfo] = useState<AddressDeleteType>(null);
    const [addressID,setAddressID]=useState('')
    const [count,setCount] = useState(0);

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
            addressCheck()
            const res = await Api.post('/address',addressInfo);
            console.log(res)
            console.log("위치정보 추가2", addressInfo);
            setAddressID(res.data.id);
            console.log("위치정보ID", res.data.id);
            dispatch(setUserAddressInfo1((res.data.id)))
            dispatch(setAddressName1((res.data.addressName)))

            // setAddressDeleteInfo((prevState) => {
            //     return {...prevState,
            //         data: {
            //             userId: info.id,
            //             addressId: addressR.addressId1
            //         }
            //     }
            // })
            console.log('store1',store)
            console.log("위치삭제정보 추가", addressDelete);
            console.log("위치삭제정보 추가2 리덕스", addressR);
            alert("추가 성공")
        }
        catch (err)
        {
            console.log(err)
            alert("추가 실패")
        }
    }
    useEffect(()=>{
        if(count){
            // dispatch(setUserAddressInfo1(addressDelete.addressId))
        }
    },[count])
    function addressCheck() {
            setAddressInfo((prevState) => {
               return {
                   ...prevState,
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
            const addressDelete2={
                data: {
                    userId: info.id,
                    addressId: addressR.addressId1
                }
            }
            const res = await Api.delete('/address', addressDelete2);
            console.log("위치정보삭제", addressDelete);
            console.log('store2',store)
            dispatch(resetaddress1())
            alert("삭제 성공")
        }
        catch (err)
        {
            console.log(err)
            alert("삭제 실패")
        }
    }

    const getAddressBtn = () => {
        getAddressData(addressID)
    }

    const inputAddressName = (e) => {
        let inputName = e.target.value;
        //한글자 이상 작성했을때
        if (inputName.length > 0) {
            setAddressInfo((prevState) => {
                return {
                    ...prevState,
                    userId: info.id,
                    addressName: e.target.value,
                    postalAddress: "나중에api",
                    latitude: JSON.stringify(location.coordinates.lat),
                    longitude: JSON.stringify(location.coordinates.lng),
                }
            })
        }


    }
    const deleteAddressBtn = () => {
        console.log("삭제버튼 클릭 ",store)
        deleteAddress(addressDelete)

    }
    // const increment = () =>{
    //     setCount(count+1);
    //     console.log("dfdfd",count)
    //     deleteAddress(addressDelete)
    //
    // };

    // useEffect(()=>{
    //     console.log("위치삭제제",addressDelete)
    //     if(addressDelete){
    //         deleteAddress(addressDelete)
    //     }
    // },[addressDelete])

    return (
        <>
            <div className={styles.box}>
                <div className={styles.gps1}>
                    <button className={styles.gps1Btn} onClick={()=>{addressPostBtn();setCount(count+1);}}>+</button>
                    <button className={styles.gps1Btn} onClick={getAddressBtn}>조회</button>
                    <button className={styles.gps1Btn} onClick={()=>{deleteAddressBtn()}}>delete</button>
                    <TextInput placeholder={"주소 이름 써"} onBlur={inputAddressName}/>
                    <div className={styles.gps1_1}>{addressR.addressName1}</div>
                    <div className={styles.gps1_1}></div>
                </div>
                <div className={styles.gps1}>
                    <button className={styles.gps1Btn} onClick={()=>{addressPostBtn();setCount(count+1);}}>+</button>
                    <button className={styles.gps1Btn} onClick={getAddressBtn}>조회</button>
                    <button className={styles.gps1Btn} onClick={()=>{deleteAddressBtn()}}>delete</button>
                    <TextInput placeholder={"주소 이름 써"} onBlur={inputAddressName}/>
                    <div className={styles.gps1_1}>{addressR.addressName1}</div>
                    <div className={styles.gps1_1}></div>
                </div>
            </div>
        </>
    );
}

export default NeighborAuth;