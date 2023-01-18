import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/MyPage.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import photo from "../img/photoSelect.png"
import PriceBox from "../component/trade/PriceBox";
import profile from "../img/profile.jpeg"
import PostContainer from "../component/trade/PostContainer";
import MyPageSet from "../component/MyPageSet";
import Card from "../component/tradeCard/Card";
import Api from "../utils/api";

interface PostType {
    content:[
        {
            postId?: number;
            title?: string;
            thumbNail?: string,
            scrapCount: number,
            tradeStatus:string,
            wishCategory?: string;
        }
    ]
}
const MyPageZZIM = () =>  {
    const [tab1, setTab] = useState('curr');
    const [scrapList,setScrapList] = useState<PostType[]>(null)

    function setDealTab(tab){
        setTab(tab)
        console.log("zzim 페이지")
        console.log(tab1)
    }
    const navigate = useNavigate();
    async function getMySrapPostList() {
        try{
            const res = await Api.get('/user/scrap');
            console.log("내 게시글들임", res.data.content[0])
            setScrapList(prevState => {
                return [ ...res.data.content];
            })
        }
        catch (err)
        {
            console.log(err)
            alert("내 스크랩 불러오기 실패");
        }
    }

    useEffect(()=>{
        getMySrapPostList();
    },[])
    if (!scrapList) {
        return null
    }
    const onClickPost = (post) => {
        navigate(`/post/${post.postId}`)
    }

    return (
        <>
            <div className={styles.MyPage}>
                <div className={styles.menu}>
                    {/*{tab1 === 'curr' ? <button className={`${styles["post"+(tab1 ==="curr"? "" : "active")]}`}  onClick={() =>{ setDealTab('curr'); navigate('/mypage');}}>d게시글</button>*/}
                    {/*    :<button className={`${styles["post"+(tab1 ==="curr"? "" : "active")]}`}  onClick={() =>{ setDealTab('curr'); navigate('/mypage');}}>게시글</button>*/}
                    {/*}*/}
                    {/*{tab1 === 'next' ? <button className={`${styles["zzim"+(tab1 ==="next"? "" : "active")]}`} onClick={() =>{ setDealTab('next'); navigate('/mypage/zzim');}}>d관심 상품</button>*/}

                    {/*    : <button className={`${styles["zzim"+(tab1 ==="next"? "" : "active")]}`} onClick={() =>{ setDealTab('next'); {tab1==="next"? navigate('/mypage/zzim'):navigate('/mypage')}}}>관심 상품</button>*/}
                    {/*}*/}
                    <button className={styles.postactive}  onClick={() =>{ setDealTab('curr'); navigate('/mypage');}}>게시글</button>
                    {/*<button className={`${styles["zzim"+(tab1 ==="next"? "" : "active")]}`} onClick={() =>{ setDealTab('next'); navigate('/mypage/zzim');}}>관심 상품</button>*/}
                    <button className={styles.zzim} onClick={()=>{ setDealTab('next')}}>관심 상품</button>
                </div>
                <div className={styles.container}>
                    {
                        scrapList.map((SingleObject:Object)=>(
                            <Card  className={"forMypage"} postTitle={SingleObject['title']} like={SingleObject['scrapCount']} wishCategory={SingleObject['wishCategory']}
                                   onClick={() => {onClickPost(SingleObject)}}/>
                        ))
                    }
                </div>
            </div>
        </>
    );
}

export default MyPageZZIM;