import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/Trade.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../../img/cokkiriLogo.png";
import TradeCategory from "../../component/trade/TradeCategory";
import PriceBox from "../../component/trade/PriceBox";
import PostContainer from "../../component/trade/PostContainer";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../index";
import {resetCategory} from "../../store/categoryReducer";


const TagSearch = () => {
    const [tab1, setTab] = useState('next');
//
    type filtertype = "recent" | "popular"
    const [filterType, setFilterType]= useState<filtertype>("recent");


    function setDealTab(tab){
        setTab(tab)
    }
    const store = useSelector((state:Rootstate) => state);

    return (
        <div className={styles.mulmulTrade}>
            <div className={styles.mulmulTradeContent}>
                    <div className={styles.navPostOrWant}>태그 검색 결과에요.</div>

                <div className={styles.popularOrNewest}>
                    {tab1 === 'next' ? <button className={`${styles["newsetBtn" + (tab1 === "next" ? "active" : "")]}`}
                                               onClick={() => {
                                                   setDealTab('next')
                                                   setFilterType('recent')
                                               }}>✓최신순</button>
                        : <button className={`${styles["newsetBtn" + (tab1 === "next" ? "active" : "")]}`}
                                  onClick={() => {
                                      setDealTab('next')
                                      setFilterType('recent')
                                  }}>최신순</button>
                    }
                    {tab1 === 'curr' ? <button className={`${styles["pupularBtn" + (tab1 === "curr" ? "active" : "")]}`}
                                               onClick={() => {
                                                   setDealTab('curr')
                                                   setFilterType('popular')
                                               }}>✓인기도순</button>
                        : <button className={`${styles["pupularBtn" + (tab1 === "curr" ? "active" : "")]}`}
                                  onClick={() => {
                                      setDealTab('curr')
                                      setFilterType('popular')
                                  }}>인기도순</button>
                    }
                </div>
                <PostContainer categoryOption = {"productCategory"} filterType={filterType}/>
            </div>
        </div>
    );
}

export default TagSearch;