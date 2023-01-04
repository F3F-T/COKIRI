import styles from "../../styles/talk/talkList.module.scss";
import React from "react";
import TalkList from "./TalkList";
import transfer from "../../img/transfer.png"


interface keyProps {
    keys: number;
}
const TalkCard = (key: keyProps) => {


    const objectMessage = [{
        keys: 1,
        title: "21fw 쿠어 MTR 발마칸 코트M (멜란지토프)",
        category: "의류",
        tradeCategry: "가구",
        url: "https://via.placeholder.com/600/771796",
        message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "보낸 사람 : 삼천원"
        ]
    },
        {
            keys: 2,
            title: "운동화",
            category: "의류",
            tradeCategry: "의류",
            url: "https://via.placeholder.com/600/92c952",
            message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원"
            ]
        },
        {
            keys: 3,
            title: "맥북",
            category: "전자 제품",
            tradeCategry: "의류",
            url: "https://via.placeholder.com/600/771796",
            message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",

            ]
        },
    ]
    const realKey = key.keys;

    return (
        <>
                    <img className={styles.cardImage} src={objectMessage[realKey-1].url }/>
                    <div className={styles.productInfo}>
                     <p className={styles.tradeState}>판매중</p>
                     <p className={styles.cardTitle}> {objectMessage[realKey-1].title} </p>
                    <div className={styles.tradeBox}>
                    <p className={styles.cardCategory}> {objectMessage[realKey-1].category} </p>
                    <img className={styles.tradeimoticon} src={transfer}/>
                    <p className={styles.cardCategory}> {objectMessage[realKey-1].tradeCategry} </p>
                    </div>
                    </div>


        </>
    )
}
export default TalkCard;