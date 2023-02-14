import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../../../styles/loginAndSignup/SettingRight.module.css"
import {
    resetaddress1, resetaddress2,
} from "../../../store/userAddressInfoReducer";
import Api from "../../../utils/api";
import {useDispatch} from "react-redux";
import {logoutUserInfo} from "../../../store/userInfoReducer";
import {useLocation, useNavigate} from "react-router-dom";
import {logoutToken} from "../../../store/jwtTokenReducer";
import {resetTalkCard} from "../../../store/talkCardReducer";

const UserDelete = () =>  {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    async function logOut() {
        try{
            const res = await Api.get('/logout');
            console.log(res);
            // alert("로그아웃");
            dispatch(logoutToken());
            dispatch(logoutUserInfo());
            dispatch(resetaddress1());
            dispatch(resetaddress2());
            dispatch(resetTalkCard());
            navigate(`/`)
        }
        catch (err)
        {
            console.log(err)
            alert("로그아웃 실패");
        }
    }
    async function deleteUser() {
        try{
            const res = await Api.delete('/user');
            alert("삭제 성공")
            logOut()
        }
        catch (err)
        {
            console.log(err)
            alert("삭제 실패")
        }
    }


    return (
        <div className={styles.box1}>
            <div className={styles.ad1}>코끼리톡 탈퇴</div>
            <div className={styles.tBox}>
                <p className={styles.t1}>• 내 프로필, 게시글 목록, 찜 목록, 그 외 사용자가 설정한 모든 정보가 사라지고복구가 불가능 합니다.</p>
                <p className={styles.t1}>• 참여 중인 모든 대화방에서 나가게 되고, 대화방에서 주고 받은 정보들은 즉시 삭제됩니다.</p>
                <p className={styles.t1}>• 거래 중이었고, 거래 완료 된 모든 물건들에 대한 정보가 사라지고 복구가 불가능 합니다.</p>
            </div>
            <button className={styles.delBtn} onClick={deleteUser}>코끼리톡 탈퇴</button>
        </div>
    );
}

export default UserDelete;