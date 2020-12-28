"use strict";

var PIECE_KING = 0; // K/k:帅/将
var PIECE_ADVISOR = 1; // A/a:仕/士
var PIECE_BISHOP = 2; // B/b:相/象
var PIECE_KNIGHT = 3 // N/n:马/马
var PIECE_ROOK = 4; // R/r:车/车
var PIECE_CANNON = 5; // C/c:砲/炮
var PIECE_PAWN = 6; // P/p:兵/卒

var RANK_TOP = 3;
var RANK_BOTTOM = 12;
var FILE_LEFT = 3;
var FILE_RIGHT = 11;

var ADD_PIECE = false;
var DEL_PIECE = true;

var IN_BOARD_ = [
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0  Y */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 1  Y */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 2  Y */
    0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, /* 3----*/
    0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, /* 4  | */
    0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, /* 5  | */
    0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, /* 6  | */
    0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, /* 7  | */
    0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, /* 8  | */
    0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, /* 9  | */
    0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, /* 10 | */
    0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, /* 11 | */
    0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, /* 12---*/
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 13 Y */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 14 Y */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 15 Y */
    /**
    0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15
    XXXXXXXXX|-----------------------|XXXXXXXXXXXXXXXXXXXXX
     */
];

/**
 * 判断sq位置是否在棋盘范围内
 * @param {} sq 
 */
function IN_BOARD(sq) {
    // sq:[0, 256)
    return IN_BOARD_[sq] != 0;
}

/**
 * 整型转字符
 * @param {} n 
 */
function CHR(n) {
    return String.fromCharCode(n);
}

/**
 * 取第一个字符，转为整型
 * @param {} c 
 */
function ASC(c) {
    return c.charCodeAt(0);
}

/**
 * 将着子步法转为形如B2-E2的字符串
 * @param {} mv 
 */
function move2Iccs(mv) {
    var sqSrc = SRC(mv);
    var sqDst = DST(mv);
    return CHR(ASC("A") + FILE_X(sqSrc) - FILE_LEFT) + 
            CHR(ASC("9") - RANK_Y(sqSrc) + RANK_TOP) + "-" +
            CHR(ASC("A") + FILE_X(sqDst) - FILE_LEFT) +
            CHR(ASC("9") - RANK_Y(sqDst) + RANK_TOP);
}

/**
 * 将字符对应棋子整型
 * @param {} c 
 */
function CHAR_TO_PIECE(c) {
    switch(c) {
        case "K":
        case "k":
            return PIECE_KING;
        case "A":
        case "a":
            return PIECE_ADVISOR;
        case "B":
        case "b":
        case "E": // Elephant
        case "e":
            return PIECE_BISHOP;
        case "N":
        case "n":
        case "H": // Horse
        case "h":
            return PIECE_KNIGHT;
        case "R":
        case "r":
            return PIECE_ROOK;
        case "C":
        case "c":
            return PIECE_CANNON;
        case "P":
        case "p":
            return PIECE_PAWN;
        default:
            return -1;
    }
}

/**
 * sq = (Y << 4) + X
 * [X,Y] : [7,12]
 * sq = (12 << 4) + 7 = 12 * 16 + 7 = 199
 * 199 >> 4 = 12
 * 199 & 15 = 7
 * [7, 12] 是 红方帅的默认位置
 */

function RANK_Y(sq) {
    return sq >> 4;
}

function FILE_X(sq) {
    return sq & 15;
}

function COORD_XY(x, y) {
    return x + (y << 4);
}

/**
 * TODO
 * 为什么是 254 - sq ?
 */
function SQUARE_FLIP(sq) {
    return 254 - sq;
}

function SIDE_TAG(sd) {
    return 8 + (sd << 3);
}

/**
 * mv = (DST << 8) + SRC
 * mv 是 move，表示一个着法
 * SRC，DST 是 sq，表示一个位置
 */

function SRC(mv) {
    return mv & 255;
}

function DST(mv) {
    return mv >> 8;
}

function MOVE(sqSrc, sqDst) {
    return sqSrc + (sqDst << 8);
}

function Position() {
}

Position.prototype.clearBoard = function() {
    this.sdPlayer = 0; // 0表示红，1表示黑
    this.squares = [];
    for (var sq = 0; sq < 256; sq ++) {
        this.squares.push(0);
    }
    // TODO
}

Position.prototype.setIrrev = function() {
    this.mvList = [0];
    this.pcList = [0];
    this.keyList = [0];
    this.chkList = [this.checked()];
    this.distance = 0;
}

Position.prototype.addPiece = function(sq, pc, bDel) {
    this.squares[sq] = bDel ? 0 : pc;
    // TODO
}

Position.prototype.changeSide = function() {
    this.sdPlayer = 1 - this.sdPlayer;
    // TODO
}

Position.prototype.movePiece = function(mv) {
    var sqSrc = SRC(mv);
    var sqDst = DST(mv);
    var pc = this.squares[sqDst];
    this.pcList.push(pc);
    if (pc > 0) {
        this.addPiece(sqDst, pc, DEL_PIECE);
    }
    pc = this.squares[sqSrc];
    this.addPiece(sqSrc, pc, DEL_PIECE);
    this.addPiece(sqDst, pc, ADD_PIECE);
    this.mvList.push(mv);
}

Position.prototype.undoMovePiece = function() {
    var mv = this.mvList.pop();
    var sqSrc = SRC(mv);
    var sqDst = DST(mv);
    var pc = this.squares[sqDst];
    this.addPiece(sqDst, pc, DEL_PIECE);
    this.addPiece(sqSrc, pc, ADD_PIECE);
    pc = this.pcList.pop();
    if (pc > 0) {
        this.addPiece(sqDst, pc, ADD_PIECE);
    }
}

Position.prototype.makeMove = function(mv) {
    // TODO : zobristKey
    this.movePiece(mv);
    if (this.checked()) {
        this.undoMovePiece();
        return false;
    }
    // TODO : keyList
    this.changeSide();
    // TODO : chkList
    // TODO : distance
    return true;
}

Position.prototype.undoMakeMove = function() {
    // TODO : distance
    // TODO : chkList
    // TODO : keyList
    this.undoMovePiece();
    this.changeSide(); // changeSide 放到最后更合适
}

/**
 * 通过fen串构造square数组等
 * fen串结构文档：http://www.xqbase.com/protocol/cchess_fen.htm
 * @param {*} fen 
 */
Position.prototype.fromFen = function(fen) {
    this.clearBoard();
    var y = RANK_TOP;
    var x = FILE_LEFT;
    var index = 0;
    if (index == fen.length) {
        this.setIrrev();
        return;
    }
    var c = fen.charAt(index);
    while (c != " ") {
        if (c == "/") {
            x = FILE_LEFT;
            y ++;
            if (y > RANK_BOTTOM) {
                break;
            }
        } else if (c >= "1" && c <= "9") {
            x += (ASC(c) - ASC("0"));
        } else if (c >= "A" && c <= "Z") { // 红
            if (x <= FILE_RIGHT) {
                var pt = CHAR_TO_PIECE(c);
                if (pt >= 0) {
                    this.addPiece(COORD_XY(x, y), pt + 8);
                }
                x ++;
            }
        } else if (c >= "a" && c <= "z") { // 黑
            if (x <= FILE_RIGHT) {
                var pt = CHAR_TO_PIECE(c);
                if (pt >= 0) {
                    this.addPiece(COORD_XY(x, y), pt + 16);
                }
                x ++;
            }
        }
        index ++;
        if (index == fen.length) {
            this.setIrrev();
            return;
        }
        c = fen.charAt(index);
    }
    index ++;
    if (index == fen.length) {
        this.setIrrev();
        return;
    }
    if (this.sdPlayer == (fen.charAt(index) == "b" ? 0 : 1)) {
        this.changeSide();
    }

    // TODO : 对于 fen串包含"moves"的情况，没有考虑
    // 比如 "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w - - 0 1 moves h2e2 h7e7"
    
    this.setIrrev();
    return;
}

Position.prototype.toFen = function() {
    var fen = "";
    for (var y = RANK_TOP; y <= RANK_BOTTOM; y ++) {
        var k = 0;
        for (var x = FILE_LEFT; x <= FILE_RIGHT; x ++) {
            var pc = this.squares[COORD_XY(x, y)];
            if (pc > 0) {
                if (k > 0) {
                    fen += CHR(ASC("0") + k);
                    k = 0;
                }
                fen += FEN_PIECE.charAt(pc);
            } else {
                k ++;
            }
        }
        if (k > 0) {
            fen += CHR(ASC("0") + k);
        }
        fen += "/";
    }
    return fen.substring(0, fen.length - 1) +
        (this.sdPlayer == 0 ? " w" : " b");
}

// TODO
Position.prototype.checked = function() {
    return false;
}
