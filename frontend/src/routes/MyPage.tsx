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
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../index";
import {log} from "util";

// interface TextInputProps {
//     init: string;
// }
interface PostType {
    userPosts:[
        {
            id?: number;
            title?: string;
            // authorNickname?: string;
            wishCategory?: string;
            content?: string;
            // productCategory?: string;
            // tradeEachOther: boolean;
            // tradeStatus: string;
        }
    ]
}
const MyPage = () =>  {
    const [tab1, setTab] = useState<string>('curr');
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const detail = useSelector((state : Rootstate)=>{return state.postDetailReducer})

    const store = useSelector((state:Rootstate) => state);
    const [postList,setPostList] = useState<PostType[]>(null)

    function setDealTab(tab) {
        setTab(tab)
        console.log(tab1)
        // return tab
    }

    async function getMyPostList() {
        //interceptor를 사용한 방식 (header에 token값 전달)
        try{
            const res = await Api.get('/user/posts');
            console.log("내 게시글들임", res.data.userPosts)
            setPostList(prevState => {
                // return [...res.data.userPosts];
                return [ ...res.data.userPosts];
            })
            console.log("내 게시글 리스트얌",postList);
            // console.log("내 게시글rdd",Object.keys(res.data.userPosts).length);

        }
        catch (err)
        {
            console.log(err)
            alert("get 실패");
        }
    }

    useEffect(()=>{
        getMyPostList();
    },[])
        // getMyPostList();

    if (!postList) {
        return null
    }
    const onClickPost = (post) => {
        navigate(`/post/${post.id}`)
    }

    return (
        <>

            <div className={styles.MyPage}>
                <div className={styles.menu}>
                    <button className={`${styles["post"+(tab1 ==="curr"? "" : "active")]}`}  onClick={() =>{ setDealTab('curr'); navigate('/mypage');}}>게시글</button>
                    <button className={styles.zzimactive} onClick={()=>{navigate('/mypage/zzim')}}>관심 상품</button>
                </div>
              <div className={styles.container}>
                  {
                      postList.map((SingleObject:Object)=>(
                      <Card  className={"forMypage"} postTitle={SingleObject['title']} like={20} wishCategory={SingleObject['wishCategory']}
                      onClick={() => {onClickPost(SingleObject)}}/>
                      ))
                      // postList.map((post)=>(
                      //     <Card  className={"forMypage"} postTitle={post['title']} like={20} wishCategory={postList['wishCategory']}
                      //            onClick={() => {onClickPost(post)}}/>
                      // ))
                      // https://velog.io/@op032/%EB%A0%8C%EB%8D%94%EB%A7%81-%EB%AC%B8%EC%A0%9C%EB%A5%BC-%ED%95%B4%EA%B2%B0%ED%95%B4%EB%B3%B4%EC%9E%90-TypeError-Cannot-read-property-title-of-undefined
                  }
              </div>

            </div>
        </>

    );
}

export default MyPage;