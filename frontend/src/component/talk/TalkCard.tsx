import styles from "../../styles/talk/talkList.module.scss";
import React from "react";
import TalkList from "./TalkList";
import transfer from "../../img/transfer.png"
import {setProductImg,setOpponetNick,setTradeStatus,setTitle,setWishCategory,setTradeCategory,resetTalkCard} from "../../store/talkCardReducer";
import {useSelector} from "react-redux";
import {Rootstate} from "../../index";



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
const TalkCard = () => {
    const talkCard = useSelector((state : Rootstate)=>{return state.talkCardReducer})

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