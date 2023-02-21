import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/home/Footer.module.css"
import {useLocation, useNavigate} from "react-router-dom";
import Card from "../component/tradeCard/Card";
import kokiri from "../img/cokkiriLogo.png";
const Footer = () =>  {

    return (
        <footer>
            <div className={styles.container}>
                <div className={styles.leftLogo}>
                    <img className={styles.logo} src={kokiri}/>
                    <p className={styles.logoText}>COKIRI</p>
                </div>
                <div className={styles.center}>
                    <div className={styles.center_1}>
                        <p className={styles.center_1_1}>회사 소개</p>
                        <p className={styles.center_1_1}>이용 약관</p>
                        <p className={styles.center_1_1}>개인정보 처리 방침</p>
                    </div>
                    <div className={styles.center_2}>
                        <p className={styles.center_2_1}>(주) 코끼리 대표이사 : 홍의성</p>
                        <p className={styles.center_2_1}>주소: 서울시 송파구 잠실로 124 3층</p>
                        <p className={styles.center_2_1}>사업자등록번호 : 487-99-03455</p>
                        <p className={styles.center_2_1}>Github</p>
                    </div>
                </div>
                <div className={styles.right}>
                    COKIRI Corp.
                </div>
            </div>

        </footer>
    );
}

export default Footer;