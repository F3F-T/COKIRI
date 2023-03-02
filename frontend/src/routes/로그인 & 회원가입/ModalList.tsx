import styles from '../../styles/loginAndSignup/ModalList.module.css'
import React, {PropsWithChildren, useState} from "react";

import {useNavigate} from "react-router-dom";
import {useSelector} from "react-redux";
import {Rootstate} from "../../index";


interface ModalDefaultType {
    onClickToggleModal: () => void;
}


function Modal({onClickToggleModal, children,}: PropsWithChildren<ModalDefaultType>)
{
    //////////모달//////////
    //주소 추가
    const navigate = useNavigate();


    const [key,setKey] = useState<number>(null)

    const onClickTotalTalkList = (key) => {
        return (event: React.MouseEvent) => {
            setKey(key);
            event.preventDefault();
        }
    }
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})




    return (
        <div className={styles.box1}>
            <div className={styles.box2}>
                <div className={styles.box2_1}>
                    <p className={styles.box2_2}>동네 인증을 먼저 진행해주세요!</p>
                    <div className={styles.box2_3}>
                        <button className={styles.btn1} onClick={()=>navigate(`/mypage/${info.id}`)}>동네 인증하기</button>
                        <button className={styles.btn2} onClick={(e: React.MouseEvent) => {
                            e.preventDefault();
                            if (onClickToggleModal) {
                                onClickToggleModal();
                            }
                        }}>그냥 둘러보기</button>
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