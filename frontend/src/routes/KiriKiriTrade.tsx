import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/trade/Trade.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../img/cokkiriLogo.png"
import TradeCategory from "../component/trade/TradeCategory";
import PriceBox from "../component/trade/PriceBox";
import PostContainer from "../component/trade/PostContainer";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../index";
import {resetCategory} from "../store/categoryReducer";


const KiriKiriTrade = () => {

    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();

    /**
     * 랜더링될때 category를 도서로 다시 초기화시킨다.
     * 이는 끼리끼리, 물물교환으로 페이지를 이동할때 도서로 초기화 시키는 역할을 함
     * */
    useEffect(()=>{
        return() =>{
            dispatch(resetCategory());
        };
    },[]);

    return (
        <div className={styles.mulmulTrade}>
            <div className={styles.mulmulTradeContent}>
                <div className={styles.categoryBox}>
                    <div className={styles.forWho}>
                        <button className={styles.forWho_1}>끼리끼리 교환해요.</button>
                    </div>
                    <TradeCategory/>
                    <PriceBox/>
                </div>
                <div className={styles.navPostOrWant}>{store.categoryReducer.category}끼리 교환해요</div>
                <div className={styles.popularOrNewest}>
                    <div className={styles.pupularBtn}>인기도순</div>
                    <div className={styles.slash}>|</div>
                    <div className={styles.newsetBtn}>누적도순</div>
                </div>
                <PostContainer/>
            </div>
        </div>
    );
}

export default KiriKiriTrade;