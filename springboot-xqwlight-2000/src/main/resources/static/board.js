"use strict";

var RESULT_UNKNOWN = 0;
var RESULT_WIN = 1;
var RESULT_DRAW = 2;
var RESULT_LOSS = 3;

var BOARD_WIDTH = 521;
var BOARD_HEIGHT = 577;
var SQUARE_SIZE = 57;
var SQUARE_LEFT = (BOARD_WIDTH - SQUARE_SIZE * 9) >> 1;
var SQUARE_TOP = (BOARD_HEIGHT - SQUARE_SIZE * 10) >> 1;
var THINKING_SIZE = 32;
var THINKING_LEFT = (BOARD_WIDTH - THINKING_SIZE) >> 1;
var THINKING_TOP = (BOARD_WIDTH - THINKING_SIZE) >> 1;

/**
 * r : 红方，b : 黑方
 * 
 * 参考：http://www.xqbase.com/protocol/cchess_move.htm
 * k : King（王）
 * a : Adivsor（士）
 * b : Bishop（象）
 * n : Knight（马）
 * r : Rook（车）
 * c : Cannon（炮）
 * p : Pawn（兵）
 * 
 * oo : 空白
 * rk : 红王
 * bk : 黑王
 * 以此类推 ...
 */
var PIECE_NAME = [
    "oo", null, null, null, null, null, null, null,
    "rk", "ra", "rb", "rn", "rr", "rc", "rp", null,
    "bk", "ba", "bb", "bn", "br", "bc", "bp", null,
];

function SQ_X(sq) {
    return SQUARE_LEFT + (FILE_X(sq) - 3) * SQUARE_SIZE;
}

function SQ_Y(sq) {
    return SQUARE_TOP + (RANK_Y(sq) - 3) * SQUARE_SIZE;
}

function Board(container, images, sounds) {
    console.log("board.js function Board");

    this.images = images; // 本地images目录
    this.sounds = sounds; // 本地sounds目录
    this.pos = new Position(); // 见position.js
    this.pos2 = new Position2();
    this.pos.fromFen("rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w - - 0 1");
    this.pos2.fromFen("rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w - - 0 1")
    this.sound = true;
    this.search = null;
    this.imageSquares = []; // len = 256, 记录每个位置img标签的数组
    this.sqSelected = 0; // 记录上一步选择的棋子
    this.mvLast = 0; // 记录上一步着法
    this.computer = -1; // 我先走:1、电脑先走:0、不用电脑:-1

    /**
     * 添加棋盘背景图
     */
    var style = container.style;
    style.position = "relative";
    style.width = BOARD_WIDTH + "px";
    style.height = BOARD_HEIGHT + "px";
    style.background = "url(" + images + "board.jpg)";

    /**
     * 逐一设置每个位置的背景图、点击事件等
     */
    var this_ = this;
    for (var sq = 0; sq < 256; sq ++) {
        if (!IN_BOARD(sq)) {
            this.imageSquares.push(null);
            continue;
        }
        var img = document.createElement("img");
        var style = img.style;
        style.position = "absolute";
        style.left = SQ_X(sq) + "px";
        style.top = SQ_Y(sq) + "px";
        style.width = SQUARE_SIZE + "px";
        style.height = SQUARE_SIZE + "px";
        style.zIndex = 0;
        img.onmousedown = function(sq_) {
            return function() {
                this_.clickSquare(sq_);
            }
        } (sq);
        container.appendChild(img);
        this.imageSquares.push(img);
    }

    this.thinking = document.createElement("img");
    this.thinking.src = images + "thinking.gif";
    style = this.thinking.style;
    style.visibility = "hidden";
    style.position = "absolute";
    style.left = THINKING_LEFT + "px";
    style.top = THINKING_TOP + "px";
    container.appendChild(this.thinking);

    // TODO dummy

    /**
     * 重新画图
     */
    this.flushBoard();
}

Board.prototype.playSound = function(soundFile) {
    if (!this.sound) {
        return;
    }
    try {
        new Audio(this.sounds + soundFile + ".wav").play();
    } catch (e) {
        console.log(e);
        // TODO dummy
    }
}

Board.prototype.setSearch = function() {
    this.search = new Search2(this.pos2, 16);
}

Board.prototype.flipped = function(sq) {
    // 如果是电脑先走，sq = 254 - sq
    return this.computer == 0 ? SQUARE_FLIP(sq) : sq;
}

/**
 * 判断当前走子方是否是电脑
 * 
 * 这里逻辑可能有点乱
 * 首先，对于this.computer来说，我先走:1、电脑先走:0、不用电脑:-1
 * 其次，由于初始fen串都是从红方开始(w)，所以sdPlayer一直为0
 * 因此，当且仅当this.computer==0时，返回结果为true，否则为false
 * 这个函数只在少数地方使用
 * 
 * 然而，从this.computer意义上看，若理解为是否是电脑先走，this.computer==1却表示我先走，有点小不妥
 */
Board.prototype.computerMove = function() {
    //console.log(this.pos.sdPlayer, this.computer);
    return this.pos.sdPlayer == this.computer;
}

Board.prototype.addMove = function(mv, computerMove) {
    // 采用了偷懒的方式，本来应该是 ? this.pos.legalMove(mv) : this.pos2.legalMove(mv);
    var legal = computerMove ? this.pos2.legalMove(mv) : this.pos2.legalMove(mv);
    if (!legal) {
        return;
    }
    this.pos.makeMove(mv);
    this.pos2.makeMove(mv);
    this.busy = true;
    if (this.mvLast > 0) {
        this.drawSquare(SRC(this.mvLast), false);
        this.drawSquare(DST(this.mvLast), false);
    }
    this.drawSquare(SRC(mv), true);
    this.drawSquare(DST(mv), true);
    this.sqSelected = 0;
    this.mvLast = mv;
    if (typeof this.onAddMove == "function") {
        this.onAddMove();
    }
    this.response();
    this.busy = false;
}

Board.prototype.response = function() {
    if (this.search == null || !this.computerMove()) {
        this.busy = false;
        return;
    }
    this.thinking.style.visibility = "visible";
    var this_ = this;
    this.busy = true;
    setTimeout(function() {
        var xhr = new XMLHttpRequest();
        xhr.open('post', 'ucci/returnint21812');
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        var loginToken = getCookie("loginToken")
        console.log(loginToken);
        xhr.setRequestHeader("Authorization", loginToken);
        console.log('fenStr: ' + this_.pos.toFen());
        xhr.send('fenStr=' + this_.pos.toFen());
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                console.log('mvResult: ' + xhr.responseText);
                var mvResult = xhr.responseText;
                //var mvResult = this_.search.searchMain(64, 10);
                this_.addMove(mvResult, true);
                this_.thinking.style.visibility = "hidden"
            } else if (xhr.status != 200) {
                console.log("http异常, readyState: " + xhr.readyState + ", status: " + xhr.status);
            }
        }
    }, 250);
}

Board.prototype.clickSquare = function(sq_) {
    // TODO busy
    var sq = this.flipped(sq_);
    var pc = this.pos.squares[sq];
    if ((pc & SIDE_TAG(this.pos.sdPlayer)) != 0) {
        this.playSound("click");
        if (this.mvLast != 0) {
            this.drawSquare(SRC(this.mvLast), false);
            this.drawSquare(DST(this.mvLast), false);
        }
        if (this.sqSelected) {
            this.drawSquare(this.sqSelected, false);
        }
        this.drawSquare(sq, true);
        this.sqSelected = sq;
    } else if(this.sqSelected > 0) {
        this.addMove(MOVE(this.sqSelected, sq), false);
    }
}

Board.prototype.drawSquare = function(sq, selected) {
    var img = this.imageSquares[this.flipped(sq)];
    img.src = this.images + PIECE_NAME[this.pos.squares[sq]] + ".gif";
    img.style.backgroundImage = selected ? "url(" + this.images + "oos.gif)" : "";
}

Board.prototype.flushBoard = function() {
    this.mvLast = this.pos.mvList[this.pos.mvList.length - 1];
    for (var sq = 0; sq < 256; sq ++) {
        if (IN_BOARD(sq)) {
            this.drawSquare(sq, sq == SRC(this.mvLast) || sq == DST(this.mvLast));
        }
    }
}

Board.prototype.restart = function(fen) {
    if (this.busy) {
        return;
    }
    this.result = RESULT_UNKNOWN;
    this.pos.fromFen(fen);
    this.pos2.fromFen(fen);
    this.flushBoard();
    this.playSound("newgame");
    this.response();
}

Board.prototype.retract = function() {
    if (this.busy) {
        return;
    }
    this.result = RESULT_UNKNOWN;
    if (this.pos.mvList.length > 1) {
        this.pos.undoMakeMove();
        this.pos2.undoMakeMove();
    }
    if (this.pos.mvList.length > 1 && this.computerMove()) {
        this.pos.undoMakeMove();
        this.pos2.undoMakeMove();
    }
    this.flushBoard();
    this.response();
}
