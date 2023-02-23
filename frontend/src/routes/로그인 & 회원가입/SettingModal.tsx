import styles from '../../styles/loginAndSignup/ProfileModal.module.css'
import React, {PropsWithChildren, useState} from "react";
import TextInput from "../../component/common/TextInput";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../index";
import {useNavigate} from "react-router-dom";
import useGeoLocation from "../../hooks/useGeolocation";
import Api from "../../utils/api";
import {
    parcelAddress1,
    parcelAddress2, resetaddress1, resetaddress2,
    setAddressName1, setAddressName2,
    setUserAddressInfo1,
    setUserAddressInfo2, setClick1, setClick2, setLat2, setLng2,setLat1, setLng1
} from "../../store/userAddressInfoReducer";
import TalkList from "../../component/talk/TalkList";
import timeConvert from "../../utils/timeConvert";
import TalkCard from "../../component/talk/TalkCard";
import Message from "../../component/talk/Message";
import {HiPencil} from "react-icons/hi";
import AddressChange from "./Settings/AddressChange";
import SignUp from "./SignUp";
import NickNameChange from "./Settings/NickNameChange";
import PasswordChange from "./Settings/PasswordChange";
import UserDelete from "./Settings/UserDelete";

interface ModalDefaultType {
    onClickToggleModal: () => void;
}
interface AddressType {
    userId:Number;
    addressName:string;
    postalAddress:string;//우편번호
    latitude:string;
    longitude:string;
}

function SettingModal({onClickToggleModal, children,}: PropsWithChildren<ModalDefaultType>)


{
    //////////모달//////////
    const [tab1, setTab] = useState<string>('curr');
    function setDealTab(tab) {
        setTab(tab)
        console.log(tab1)
        // return tab
    }

    const addressR = useSelector((state : Rootstate)=>{return state.userAddressInfoReducer})


    const [selectList,setSelectList] = useState(1);
    console.log("주소1이 잘 들어갔나",addressR)

    //주소 추가


    const [key,setKey] = useState<number>(null)

    const onClickTotalTalkList = (key) => {
        return (event: React.MouseEvent) => {
            setKey(key);
            event.preventDefault();
        }
    }



    return (
        <div className={styles.box1}>
            <div className={styles.box2}>

                <div className={styles.left}>
                    <button className={styles.left_1} onClick={()=>{setSelectList(1)}}>닉네임 변경</button>
                    <button className={styles.left_1} onClick={()=>{setSelectList(2)}}>비밀번호 변경</button>
                    <button className={styles.left_1} onClick={()=>{setSelectList(3)}}>주소 변경</button>
                    {/*<button className={styles.left_1} onClick={()=>{setSelectList(4)}}>로그아웃</button>*/}
                    <button className={styles.left_1} onClick={()=>{setSelectList(5)}}>계정 탈퇴</button>
                    <div className={styles.left_2}>
                        <div className={styles.left_21}>COKIRI</div>
                        <div className={styles.left_22}>계정 센터</div>
                        <div className={styles.left_23}>개인정보 변경 및 회원 탈퇴 등 COKIRI 설정을 관리하세요.</div>
                    </div>

                </div>
                <div className={styles.right}>
                    {selectList ==1?
                        <NickNameChange/>:<></>
                    }
                    {selectList ==2?
                        <PasswordChange/>:<></>
                    }
                    {selectList ==3?
                        <AddressChange/>:<></>
                    }
                    {selectList ==5?
                        <UserDelete/>:<></>
                    }

                </div>
            </div>
            <div className={styles.box3}
                onClick={(e: React.MouseEvent) => {
                    e.preventDefault();
                    if (onClickToggleModal) {
                            onClickToggleModal();
                    }
                }}
            />
        </div>
    );
}

export default SettingModal