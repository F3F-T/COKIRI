import React, {useState, useRef,useEffect, useMemo, useCallback} from 'react';
import styles from "../styles/loginAndSignup/MyPage.module.css"
import {useLocation, useNavigate} from "react-router-dom";
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
    content:[
        {
            postId?: number;
            title?: string;
            thumbNail?: string,
            likeCount: number,
            tradeStatus:string,
            wishCategory?: string;
        }
    ]
}
const MyPage = () =>  {
    const [tab1, setTab] = useState<string>('curr');
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const {state} = useLocation(); //다른 유저꺼 받을 때
    console.log("포스트아이디 이거로 넘어온건지 확인", state)
    const [otherPost,setOtherPostList] = useState(null)
    console.log("다른 유저 게시글 정보222222222",otherPost)

    const detail = useSelector((state : Rootstate)=>{return state.postDetailReducer})
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const store = useSelector((state:Rootstate) => state);
    const [postList,setPostList] = useState<PostType[]>(null)

    function setDealTab(tab) {
        setTab(tab)
        console.log(tab1)
        // return tab
    }



    async function getMyPostList() {
        try{
            const res = await Api.get('/user/posts');
            console.log("내 게시글들임", res.data.content[0])
            setPostList(prevState => {
                return [ ...res.data.content];
            })
        }
        catch (err)
        {
            console.log(err)
            alert("내 게시글 불러오기 실패");
        }
    }
    async function getUserPost_2(){
            try{
                const res = await  Api.get(`/post/user/${state}`)
                console.log("다른 유저 게시글 정보",res)
                setOtherPostList(prevState => {
                    return [ ...res.data.content];
                })

            }
            catch (err){
                console.log(err);
                alert("실패인가")
            }
    }
    useEffect(()=>{
        if(state==null || state == info.id) {
            getMyPostList();
        }
    },[])
    useEffect(()=>{
        if(state!=null && state != info.id){
            getUserPost_2();
        }
    },[])
        // getMyPostList();

    if(state==null || state == info.id){
        if (!postList) {
            return null
        }
    }

    if(state!=null && state != info.id){
        if (!otherPost) {
            return null
        }
    }


    const onClickPost = (post) => {
        navigate(`/post/${post.postId}`)
    }

    return (
        <>

            <div className={styles.MyPage}>
                {/*<div className={styles.menu}>*/}
                {/*    <button className={`${styles["post"+(tab1 ==="curr"? "" : "active")]}`}  onClick={() =>{ setDealTab('curr'); navigate('/mypage');}}>게시글</button>*/}
                {/*    <button className={styles.zzimactive} onClick={()=>{navigate('/mypage/zzim')}}>관심 상품</button>*/}
                {/*</div>*/}
              <div className={styles.container}>
                  {

                      state ==null?
                          postList.reverse().map((SingleObject:Object)=>(
                              <Card  className={"forMypage"} postTitle={SingleObject['title']} like={SingleObject['likeCount']} wishCategory={SingleObject['wishCategory']} thumbnail={SingleObject['thumbNail']}
                                     onClick={() => {onClickPost(SingleObject)}}/>
                          ))


                          :

                          info.id==state?
                              postList.reverse().map((SingleObject:Object)=>(
                                  <Card  className={"forMypage"} postTitle={SingleObject['title']} like={SingleObject['likeCount']} wishCategory={SingleObject['wishCategory']} thumbnail={SingleObject['thumbNail']}
                                         onClick={() => {onClickPost(SingleObject)}}/>
                              )):
                              otherPost.reverse().map((SingleObject:Object)=>(
                                  <Card  className={"forMypage"} postTitle={SingleObject['title']} like={SingleObject['likeCount']} wishCategory={SingleObject['wishCategory']} thumbnail={SingleObject['thumbNail']}
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