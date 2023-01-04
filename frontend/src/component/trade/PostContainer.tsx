import React, {useState, useEffect, useMemo, useCallback} from 'react';
import styles from "../../styles/trade/PostContainer.module.css"
import {useNavigate} from "react-router-dom";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import myImage from "../../img/cokkiriLogo.png"
import Card from "../tradeCard/Card"
import Comments from "../comments/Comments";
import {storeCategory} from "../../store/categoryReducer";
import {useDispatch, useSelector} from "react-redux";
import {storePostDetail} from "../../store/postDetailReducer";
import TalkList from "../talk/TalkList";
import {Rootstate} from "../../index";
import tradeEx from "../../img/tradeEx.jpeg";
import transfer from "../../img/transfer.png";
import styled from "../../styles/card/cards.module.scss";
import Api from "../../utils/api";
import {setToken} from "../../store/jwtTokenReducer";
import {setUserInfo} from "../../store/userInfoReducer";
import {log} from "util";

interface PostType {
    postId : number;
    title : string;
    content : string;
    tradeEachOther : boolean;
    authorNickname : string;
    wishCategory : string;
    productCategory : string;
    tradeStatus : string;
    tagNames : string[];
}


const PostContainer = () => {

    // const [postList,setPostList] = useState<PostType[]>()

    const dispatch = useDispatch();
    const navigate = useNavigate();
    const onClickTotalTalkList = (key) => {
        return (event: React.MouseEvent) => {
            onClickPostDetail(key)
            event.preventDefault();
        }
    }
    const postDetail =[
        {
            keys: 1,
            postTitle : "21fw쿠어 MTR 발마칸 코트Msdfsdkbkhug",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },
        {
            keys: 2,
            postTitle : "코트",
            postContent : "남성의류",
            like : "1",
            comment : "4",
            category: "의류"
        },
        {
            keys: 3,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },
        {
            keys: 4,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },
        {
            keys: 5,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },        {
            keys: 6,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },        {
            keys: 7,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },        {
            keys: 8,
            postTitle : "스팸",
            postContent : "남성의류",
            like : "3",
            comment : "8",
            category: "가구"
        },
    ]

    const detail = useSelector((state : Rootstate)=>{return state.postDetailReducer})

    const store = useSelector((state:Rootstate) => state);

    const [postList,setPostList] = useState<PostType[]>()
    async function getPostList() {

        //interceptor를 사용한 방식 (header에 token값 전달)
        try{
            const res = await Api.get('/post');
            console.log(res.data)

            setPostList(prevState => {
                return [...res.data];
            })

            // let newData = res.data.map((post:PostType) => {
            //     setPostList(prevState => {
            //         return [...prevState];
            //     })
            // });

            // let newData = res.data.map((data) => {
            //     setTest(prevState => {
            //         return [...prevState, "hi"];
            //     })
            // });

            // setTest(["hi","hi2"])
            // setTest(prevState => {
            //     return [...prevState, "hi3"]
            // })
            //
            // setTest2({key1: "hi1", key2 : "hi2"})
            // setTest2(prevState => {
            //     return [{...prevState, key3: "hi3"}, {key3 : "hi3"}]
            // })


            console.log(postList);

        }
        catch (err)
        {
            console.log(err)
            alert("get 실패");
        }

    }

    useEffect(()=>{
        getPostList();
    },[])

    const onClickTest = () => {

        console.log(postList)

    }

    const onClickPostDetail = (i)  => {
        dispatch(storePostDetail(postDetail[i].postTitle))
        dispatch(storePostDetail(postDetail[i].like))
        dispatch(storePostDetail(postDetail[i].comment))
        dispatch(storePostDetail(postDetail[i].category))

        //확인하고 싶을때 : console.log(store.categoryReducer.category)
        //store : index.tsx에 있는 store
        //categoryReducer : store ->  reducer안에 선언한 categoryReducer, 이는 categoryReducer.ts를 참조한다.
        //category : categoryReducer.ts 안에 있는 categorySlice에 담긴 category이다. 이는 initialState : 안에 선언이 되어있다.
    }

        return (
        <div className={styles.postContainer}>
            <button onClick={onClickTest}/>

            {
                postList.map((post:object)=>(
                    <Card className={"forTrade"} postTitle={SingleObject["postTitle"]} postContent={SingleObject["postContent"]} like={SingleObject["like"]} comment={SingleObject["comment"]} category={SingleObject["category"]} />
                ))


                postDetail.map((SingleObject:object)=>(
                    <Card className={"forTrade"} postTitle={SingleObject["postTitle"]} postContent={SingleObject["postContent"]} like={SingleObject["like"]} comment={SingleObject["comment"]} category={SingleObject["category"]} />
                ))
            }
            <Card className={"forTrade"} postTitle={detail.postTitle} postContent={detail.postContent} like={detail.like} comment={detail.comment} category={detail.category} />

        </div>
    );
}

export default PostContainer;