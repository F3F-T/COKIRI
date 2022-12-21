import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/Trade.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../../img/cokkiriLogo.png";
import TradeCategory from "./TradeCategory";
import PriceBox from "./PriceBox";
import PostContainer from "./PostContainer";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../index";



const MulmulTrade1 = () => {
    const store = useSelector((state:Rootstate) => state);
    const dispatch = useDispatch();

    let navigate = useNavigate();

    console.log(store.categoryReducer.category)
    return (
        <div className={styles.mulmulTrade}>
            <div className={styles.mulmulTradeContent}>
                <div className={styles.navPostOrWant}>{store.categoryReducer.category}를 올린 사람들이에요</div>
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

const MulmulTrade2 = () => {
    let navigate = useNavigate();
    return (
        <div className={styles.mulmulTrade}>
            <div className={styles.mulmulTradeContent}>
                <div className={styles.navPostOrWant}>도서를 원하는 사람들이에요.</div>
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

const ItemList = () => {

}

export default MulmulTrade1;