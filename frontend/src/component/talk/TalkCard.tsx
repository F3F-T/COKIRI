import styles from "../../styles/talk/talkList.module.scss";
import React, {useEffect, useState} from "react";
import TalkList from "./TalkList";
import transfer from "../../img/transfer.png"
import {
    setProductImg,
    setOpponetNick,
    setTradeStatus,
    setTitle,
    setWishCategory,
    setTradeCategory,
    resetTalkCard,
    setPostId, setMessageRoomId, setBuyerId, setSellerId
} from "../../store/talkCardReducer";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../index";
import Api from "../../utils/api";



interface PostType {
    post : {
        id? : number;
        title? : string;
        content? : string;
        price?: string;
        tradeEachOther? : boolean;
        authorNickname? : string;
        wishCategory? : string;
        productCategory? : string;
        tradeStatus? : string;
        tagNames? : string[];
        scrapCount? : number;
        messageRoomCount? : number;
        createdTime? : string;
        images?:[
            {id:number, imgPath: string}
        ]
    }


}
interface keyProps {
    keys: number;
}
const TalkCard= (key: keyProps) => {
    const talkCard = useSelector((state : Rootstate)=>{return state.talkCardReducer})
    const dispatch = useDispatch();
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const realKey = key.keys;
    // const objectMessage = [{
    //     keys: 1,
    //     title: "21fw 쿠어 MTR 발마칸 코트M (멜란지토프)",
    //     category: "의류",
    //     tradeCategry: "가구",
    //     url: "https://via.placeholder.com/600/771796",
    //     message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //         "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //         "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //         "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //         "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //         "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //         "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //         "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //         "보낸 사람 : 이거 얼마", "보낸 사람 : 삼천원"
    //     ]
    // },
    //     {
    //         keys: 2,
    //         title: "운동화",
    //         category: "의류",
    //         tradeCategry: "의류",
    //         url: "https://via.placeholder.com/600/92c952",
    //         message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //             "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //             "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //             "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원"
    //         ]
    //     },
    //     {
    //         keys: 3,
    //         title: "맥북",
    //         category: "전자 제품",
    //         tradeCategry: "의류",
    //         url: "https://via.placeholder.com/600/771796",
    //         message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //             "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //             "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //             "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
    //
    //         ]
    //     },
    // ]
    // const realKey = key.id;
    useEffect(()=>{
        getMessageRoom()
    },[realKey])

    async function getMessageRoom() {
        try{
            const res = await Api.get('/user/messageRooms');
            console.log("메세지룸 조회", res.data.content)
            const res2 = await Api.get(`/user/${info.id}/totalMessageRooms`);
            console.log("메세지룸 조회2",res2.data)
            // dispatch(setMessageRoomId(res.data.content.messageRoomId))

            for(let i=0;i<res2.data.length;i++){
                if(res2.data[i].id == realKey){
                    const res3 = await Api.get(`/post/${res2.data[i].postId}`)
                    dispatch(setTradeStatus(res3.data.tradeStatus))
                    dispatch(setTradeCategory(res3.data.tradeCategory))
                    dispatch(setWishCategory(res3.data.wishCategory))
                    dispatch(setProductImg(res3.data.images[0].imgPath))
                    dispatch(setTitle(res3.data.title))

                    dispatch(setPostId(res2.data[i].postId))
                    dispatch(setMessageRoomId(res2.data[i].id))
                    dispatch(setBuyerId(res2.data[i].buyerId))
                    dispatch(setSellerId(res2.data[i].sellerId))
                    if(info.id === res2.data[i].buyerId){
                        dispatch(setOpponetNick(res2.data[i].sellerNickName))
                    }
                    else{
                        dispatch(setOpponetNick(res2.data[i].buyerNickName))
                    }
                }
            }
            // alert("메세지룸 조회 성공 in message")
        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 조회 실패 in message")
        }
    }
    console.log("잘잘잘ㅈ왔니 ",talkCard)

    return (
        <>
                    <img className={styles.cardImage} src={talkCard.productImg}/>
                    <div className={styles.productInfo}>
                     <p className={styles.tradeState}>{talkCard.tradeStatus}</p>
                     <p className={styles.cardTitle}> {talkCard.title} </p>
                    <div className={styles.tradeBox}>
                    <p className={styles.cardCategory}> {talkCard.tradeCategory} </p>
                    <img className={styles.tradeimoticon} src={transfer}/>
                    <p className={styles.cardCategory}> {talkCard.wishCategory} </p>
                    </div>
                    </div>

        </>
    )
}
export default TalkCard;