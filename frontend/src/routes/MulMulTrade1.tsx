import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/trade/Trade.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../img/cokkiriLogo.png";
import TradeCategory from "../component/trade/TradeCategory";
import PriceBox from "../component/trade/PriceBox";
import PostContainer from "../component/trade/PostContainer";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../index";
import {resetCategory} from "../store/categoryReducer";


const MulmulTrade1 = () => {
    const [tab1, setTab] = useState('curr');
    function setDealTab(tab){
        setTab(tab)
    }
    const store = useSelector((state:Rootstate) => state);
    const categories2: string[]=
        ['생활가전',  '유아동',  '뷰티/미용',
            '티켓/교환권', '식물', '반려동물용품', '가공용품', '취미/게임', '생활/주방']
    return (
        <div className={styles.mulmulTrade}>
            <div className={styles.mulmulTradeContent}>
                { categories2.includes(store.categoryReducer.category)?
                    <div className={styles.navPostOrWant}>{store.categoryReducer.category}을 올린 사람들이에요</div> :
                    <div className={styles.navPostOrWant}>{store.categoryReducer.category}를 올린 사람들이에요</div>
                }
                <div className={styles.popularOrNewest}>
                    {tab1 === 'curr' ? <button className={`${styles["pupularBtn"+(tab1 ==="curr"? "active" : "")]}`}  onClick={() =>{ setDealTab('curr')}}>✓인기도순</button>
                    : <button className={`${styles["pupularBtn"+(tab1 ==="curr"? "active" : "")]}`}  onClick={() =>{ setDealTab('curr')}}>인기도순</button>
                    }
                    {tab1 === 'next' ? <button className={`${styles["newsetBtn"+(tab1 ==="next"? "active" : "")]}`} onClick={() =>{ setDealTab('next')}}>✓누적도순</button>
                        : <button className={`${styles["newsetBtn"+(tab1 ==="next"? "active" : "")]}`} onClick={() =>{ setDealTab('next')}}>누적도순</button>
                    }
                </div>
                <PostContainer/>
            </div>
        </div>
    );
}

export default MulmulTrade1;