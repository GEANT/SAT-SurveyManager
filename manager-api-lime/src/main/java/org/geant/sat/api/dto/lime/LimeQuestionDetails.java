/*
 * GÉANT BSD Software License
 *
 * Copyright (c) 2017 - 2020, GÉANT
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the GÉANT nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * Disclaimer:
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.geant.sat.api.dto.lime;

import com.google.gson.annotations.SerializedName;

/**
 * The wrapper class for Limesurvey question details.
 */
public class LimeQuestionDetails {

    /** The identifier. */
    private Object id;

    /** The question identifier. */
    private String qid;

    /** The parent question identifier. */
    @SerializedName("parent_qid")
    private String parentQid;

    /** The survey identifier. */
    private String sid;

    /** The group identifier. */
    private String gid;

    /** The question type. */
    private String type;

    /** The question title. */
    private String title;

    /** The question. */
    private String question;

    /** Preg (unknown) value in Limesurvey. */
    private String preg;

    /** The help for the question. */
    private String help;

    /** Other (unknown) value in Limesurvey. */
    private String other;

    /** Whether the question is mandatory. */
    private String mandatory;

    /** The question order. */
    @SerializedName("question_order")
    private String questionOrder;

    /** The question language. */
    private String language;

    /** Scale_id (unknown) value in Limesurvey. */
    @SerializedName("scale_id")
    private String scaleId;

    /** Same_default (unknown) value in Limesurvey. */
    @SerializedName("same_default")
    private String sameDefault;

    /** The question relevance. */
    private String relevance;

    /** The module name for the question. */
    @SerializedName("modulename")
    private String moduleName;

    /**
     * Get the identifier.
     * @return The identifier.
     */
    public Object getId() {
        return id;
    }

    /**
     * Set the identifier.
     * @param newId What to set.
     */
    public void setId(Object newId) {
        this.id = newId;
    }

    /**
     * Get the question identifier.
     * @return The question identifier.
     */
    public String getQid() {
        return qid;
    }

    /**
     * Set the question identifier.
     * @param newQid What to set.
     */
    public void setQid(String newQid) {
        this.qid = newQid;
    }

    /**
     * Get the parent question identifier.
     * @return The parent question identifier.
     */
    public String getParentQid() {
        return parentQid;
    }

    /**
     * Set the parent question identifier.
     * @param newParentQid What to set.
     */
    public void setParentQid(String newParentQid) {
        this.parentQid = newParentQid;
    }

    /**
     * Get the survey identifier.
     * @return The survey identifier.
     */
    public String getSid() {
        return sid;
    }

    /**
     * Set the survey identifier.
     * @param newSid What to set.
     */
    public void setSid(String newSid) {
        this.sid = newSid;
    }

    /**
     * Get the group identifier.
     * @return The group identifier.
     */
    public String getGid() {
        return gid;
    }

    /**
     * Set the group identifier.
     * @param newGid What to set.
     */
    public void setGid(String newGid) {
        this.gid = newGid;
    }

    /**
     * Get the question type.
     * @return The question type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the question type.
     * @param newType What to set.
     */
    public void setType(String newType) {
        this.type = newType;
    }

    /** 
     * Get the question title.
     * @return The question title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the question title.
     * @param newTitle What to set.
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     * Get the question.
     * @return The question.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Set the question.
     * @param newQuestion What to set.
     */
    public void setQuestion(String newQuestion) {
        this.question = newQuestion;
    }

    /**
     * Get preg (unknown) value in Limesurvey.
     * @return preg (unknown) value in Limesurvey.
     */
    public String getPreg() {
        return preg;
    }

    /**
     * Set preg (unknown) value in Limesurvey.
     * @param newPreg What to set.
     */
    public void setPreg(String newPreg) {
        this.preg = newPreg;
    }

    /**
     * Get the question help.
     * @return The question help.
     */
    public String getHelp() {
        return help;
    }

    /**
     * Set the question help.
     * @param newHelp What to set.
     */
    public void setHelp(String newHelp) {
        this.help = newHelp;
    }

    /**
     * Get other (unknown) value in Limesurvey.
     * @return other (unknown) value in Limesurvey.
     */
    public String getOther() {
        return other;
    }

    /**
     * Set other (unknown) value in Limesurvey.
     * @param newOther What to set.
     */
    public void setOther(String newOther) {
        this.other = newOther;
    }

    /**
     * Get whether the question is mandatory.
     * @return Whether the question is mandatory.
     */
    public String getMandatory() {
        return mandatory;
    }

    /**
     * Set whether the question is mandatory.
     * @param newMandatory What to set.
     */
    public void setMandatory(String newMandatory) {
        this.mandatory = newMandatory;
    }

    /**
     * Get the question order.
     * @return The question order.
     */
    public String getQuestionOrder() {
        return questionOrder;
    }

    /**
     * Set the question order.
     * @param newQuestionOrder What to set.
     */
    public void setQuestionOrder(String newQuestionOrder) {
        this.questionOrder = newQuestionOrder;
    }

    /**
     * Get the question language.
     * @return The question language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the question language.
     * @param newLanguage What to set.
     */
    public void setLanguage(String newLanguage) {
        this.language = newLanguage;
    }

    /**
     * Get scale_id (unknown) value in Limesurvey.
     * @return scale_id (unknown) value in Limesurvey.
     */
    public String getScaleId() {
        return scaleId;
    }

    /**
     * Set scale_id (unknown) value in Limesurvey.
     * @param newScaleId What to set.
     */
    public void setScaleId(String newScaleId) {
        this.scaleId = newScaleId;
    }

    /**
     * Get same_default (unknown) value in Limesurvey.
     * @return same_default (unknown) value in Limesurvey.
     */
    public String getSameDefault() {
        return sameDefault;
    }

    /**
     * Set same_default (unknown) value in Limesurvey.
     * @param newSameDefault What to set.
     */
    public void setSameDefault(String newSameDefault) {
        this.sameDefault = newSameDefault;
    }

    /**
     * Get the question relevance.
     * @return The question relevance.
     */
    public String getRelevance() {
        return relevance;
    }

    /**
     * Set the question relevance.
     * @param newRelevance What to set.
     */
    public void setRelevance(String newRelevance) {
        this.relevance = newRelevance;
    }

    /**
     * Get the question module name.
     * @return The question module name.
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Set the question module name.
     * @param newModuleName What to set.
     */
    public void setModuleName(String newModuleName) {
        this.moduleName = newModuleName;
    }

}
