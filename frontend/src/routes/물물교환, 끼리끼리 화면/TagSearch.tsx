import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/Trade.module.css"
import {useNavigate, useParams} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../../img/cokkiriLogo.png";
import TradeCategory from "../../component/trade/TradeCategory";
import PriceBox from "../../component/trade/PriceBox";
import PostContainer from "../../component/trade/PostContainer";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../index";
import {resetCategory} from "../../store/categoryReducer";
import queryString from 'query-string';
import Comments from "../../component/comments/Comments";
import timeConvert from "../../utils/timeConvert";


const TagSearch = () => {
    const [tab1, setTab] = useState('next');
//
    type filtertype = "recent" | "popular"
    const [filterType, setFilterType] = useState<filtertype>("recent");
    const params = useParams();
    const queryStringUnParsed = window.location.search
    const queryStringParsed = queryString.parse(queryStringUnParsed);
    const tagArray = queryStringParsed.tags.toString().split(',');
    console.log(tagArray);

    function setDealTab(tab) {
        setTab(tab)
    }

    const store = useSelector((state: Rootstate) => state);

    return (
        <div className={styles.mulmulTrade}>
            <div className={styles.mulmulTradeContent}>

                <div className={styles.navPostOrWant}>태그 검색 결과에요.</div>
                <div className = {styles.tagDiv}>
                    {
                        tagArray.map((tag) => (
                            <span className={styles.tagNames}>#{tag} </span>
                        ))
                    }
                </div>

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
                <PostContainer searchOption={"tag"} filterType={filterType}/>
            </div>
        </div>
    );
}

export default TagSearch;