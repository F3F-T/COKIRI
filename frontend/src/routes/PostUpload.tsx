import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/PostUpload.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import photo from "../img/photoSelect.png"
import PriceBox from "../component/trade/PriceBox";


const PostUpload = () => {
    const navigate = useNavigate();

    const signUpButtonClick = () => {
        navigate(`/signup/emailcheck`)
    }

    return (
        <div className={styles.postBox}>
        <div className={styles.postUpload}>
            <div className={styles.header}>
                <p className={styles.header_1}>기본 정보</p>
                <p className={styles.header_2}>* 필수 정보</p>
            </div>
            <div className={styles.container}>
                <div className={styles.item1}>
                    <img className={styles.photos} src={photo}/>
                </div>
                <div className={styles.item2}>
                    <p className={styles.star}>*</p><input type="text" className={styles.item2_2} placeholder="글 제목을 적어주세요."/>
                </div>
                <div className={styles.item2}>
                    <p className={styles.star}>*</p><input type="text" className={styles.item2_2} placeholder="생각하는 물건의 가격대를 적어주세요."/>
                </div>
                <div className={styles.item2}>
                    <p className={styles.star}>*</p>
                    <textarea className={styles.item2_2} cols={50} rows={6} placeholder="상도1동에 올릴 게시글을 적어주세요."/>
                </div>
                <div className={styles.categoryBox}>
                    <p className={styles.star}>*</p>
                    <p className={styles.categoryText}>올릴 물건의 카테고리를 선택해주세요.</p>
                </div>
                <div className={styles.categoryBox2}>
                    <select className={styles.categoryToggle} placeholder="올릴 물건의 카테고리를 선택해주세요." >
                        <option value="의류">의류</option>
                        <option value="도서">도서</option>
                        <option value="티켓">티켓</option>
                    </select>
                </div>
                <div className={styles.categoryBox}>
                    <p className={styles.categoryText2}>원하는 물건의 카테고리를 선택해주세요.</p>
                </div>
                <div className={styles.categoryBox2}>
                    <select className={styles.categoryToggle} placeholder="원하는 물건의 카테고리를 선택해주세요." >
                        <option value="의류">의류</option>
                        <option value="도서">도서</option>
                        <option value="티켓">티켓</option>
                    </select>
                </div>
                <div className={styles.item2}>
                   <input type="text" className={styles.item2_1} placeholder="해시태그를 적어주세요."/>
                </div>

            </div>
            <div className={styles.btnPlace}>
                <button className={styles.uploadBtn}>내 물건 올리기</button>
            </div>


        </div>
        </div>


    );

}

export default PostUpload;