import styles from '../../styles/loginAndSignup/googleLoginModal.module.css'
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
function Modal({onClickToggleModal, children,}: PropsWithChildren<ModalDefaultType>)


{
    //////////모달//////////
    const [tab1, setTab] = useState<string>('curr');
    function setDealTab(tab) {
        setTab(tab)
        console.log(tab1)
        // return tab
    }
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
    const [count1,setCount1]=useState(0);
    const [count2,setCount2]=useState(0);

    console.log("주소1이 잘 들어갔나",addressR)

    //주소 추가
    async function postAddressData_1() {
        try{
            dispatch(parcelAddress1(parcel_1))
            const res = await Api.post('/address',addressInfo);
            console.log("첫번째 위치정보", addressInfo);
            setAddressID(res.data.id);
            dispatch(setUserAddressInfo1((res.data.id)))
            dispatch(setAddressName1((res.data.addressName)))
            dispatch(setClick1(1))
            dispatch(setLat1(JSON.stringify(location.coordinates.lat)))
            dispatch(setLng1(JSON.stringify(location.coordinates.lng)))

            // alert("추가 성공")
        }
        catch (err)
        {
            console.log(err)
            alert("추가 실패")
        }
    }
    async function postAddressData_2() {
        try{
            dispatch(parcelAddress2(parcel_2))
            const res = await Api.post('/address',addressInfo);
            console.log(res)
            console.log("두번째 위치정보", addressInfo);
            setAddressID(res.data.id);
            dispatch(setUserAddressInfo2((res.data.id)))
            dispatch(setAddressName2((res.data.addressName)))
            dispatch(setClick2(1))
            dispatch(setLat2(JSON.stringify(location.coordinates.lat)))
            dispatch(setLng2(JSON.stringify(location.coordinates.lng)))

            // alert("추가 성공")
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
            // alert("삭제 성공")
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
            // alert("삭제 성공")
        }
        catch (err)
        {
            console.log(err)
            alert("삭제 실패")
        }
    }

    //input기능
    const inputAddressName_1 = (e) => {
        console.log("대답1")
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
        console.log("대답2")
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

    //카카오 지도로 지번주소 불러오기
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
                setParcel_1(_arr)
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
                setParcel_2(_arr)
            }
        }
        geocoder.coord2Address(coord.getLng(), coord.getLat(), callback);
    }



    return (
        <div className={styles.box1}>
            <div className={styles.box2}>
                {/*{children}*/}
                <div className={styles.addressBox}>
                    <div className={styles.addressBox_1}>
                        <p className={styles.ad1}> 동네 인증하기</p>
                        <div className={styles.add2Box}>
                            <p className={styles.ad2}>동네 인증은 현재 위치로만 가능해요. (최대 2개까지 등록할 수 있어요)</p>
                        </div>
                    </div>
                    <div className={styles.plusBtnBox}>
                        {/*<button className={styles.plusBtn1}>첫번째 주소를 추가해주세요</button>*/}
                        {/*<button className={styles.plusBtn2}>두번째 주소를 추가해주세요</button>*/}
                        {tab1 === 'curr' ? <button className={`${styles["plusBtn1"+(tab1 ==="curr"? "active" : "")]}`}  onClick={() =>{ setDealTab('curr')}}>첫번째 주소를 추가해주세요</button>
                            : <button className={`${styles["plusBtn1"+(tab1 ==="curr"? "active" : "")]}`}  onClick={() =>{ setDealTab('curr')}}>첫번째 주소를 추가해주세요</button>
                        }
                        {tab1 === 'next' ? <button className={`${styles["plusBtn2"+(tab1 ==="next"? "active" : "")]}`} onClick={() =>{ setDealTab('next')}}>두번째 주소를 추가해주세요</button>
                            : <button className={`${styles["plusBtn2"+(tab1 ==="next"? "active" : "")]}`} onClick={() =>{ setDealTab('next')}}>두번째 주소를 추가해주세요</button>
                        }

                    </div>
                    <div className={styles.registerBox}>
                        <div className={styles.inputBox}>
                            {tab1 === 'curr' ?
                                    addressR.parcelName1==undefined?
                                    <>  <p className={styles.input1}>주소 이름</p>
                                        <input type="text" placeholder={'첫번째 주소 이름을 적어주세요.       [ 예시:  집, 회사 ]'}  className={`${styles["input2"+(tab1 ==="curr"? "active" : "")]}`} onChange={inputAddressName_1}/>
                                        <button className={styles.registerBtn} onClick={()=>{postAddressData_1();setCount1(count1+1)}}>등록</button>
                                    </>:
                                    <>
                                        <p className={styles.input1}>지번 주소</p>
                                        <div className={styles.input2}>{addressR.parcelName1}</div>
                                        <button className={styles.registerBtn} onClick={deleteAddress_1}>삭제</button>
                                    </>

                                :

                                addressR.parcelName2==undefined?
                                    <>  <p className={styles.input1}>주소 이름</p>
                                        <input type="text" placeholder={'두번째 주소 이름을 적어주세요.       [ 예시:  집, 회사 ]'}  className={`${styles["input2_2"+(tab1 ==="curr"? "active" : "")]}`} onChange={inputAddressName_2} />
                                        <button className={styles.registerBtn} onClick={()=>{postAddressData_2();setCount2(count2+1)}}>등록</button>
                                    </>:
                                    <>
                                        <p className={styles.input1}>지번 주소</p>
                                        <div className={styles.input2}>{addressR.parcelName2}</div>
                                        <button className={styles.registerBtn} onClick={deleteAddress_2}>삭제</button>

                                    </>
                            }
                            {/*<input type="text" placeholder={'첫번째 주소 이름을 적어주세요.       [ 예시:  집, 회사 ]'} className={styles.input2}/>*/}
                        </div>
                        {/*<div className={styles.outputBox}>*/}
                        {/*    <p className={styles.input1}>지번주소</p>*/}
                        {/*    <div className={styles.parcel}>주소자리</div>*/}
                        {/*</div>*/}
                    </div>
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
export default Modal