package io.xtech.recognizers;

import com.google.common.collect.Lists;
import com.microsoft.recognizers.text.ModelResult;
import com.microsoft.recognizers.text.number.NumberRecognizer;
import com.microsoft.recognizers.text.numberwithunit.NumberWithUnitRecognizer;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.xtech.recognizers.models.RecognizeInput;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Controller("/recognize")
public class RecognizeController {
    @Post(value = "/currency", produces = MediaType.APPLICATION_JSON)
    public List<ModelResult> recognizeCurrency(@Body RecognizeInput recognizeInput) {
        return NumberWithUnitRecognizer.recognizeCurrency(recognizeInput.getText(), recognizeInput.getCulture());
    }

    @Post(value = "/range", produces = MediaType.APPLICATION_JSON)
    public List<ModelResult> recognizeRange(@Body RecognizeInput recognizeInput) {
        return NumberRecognizer.recognizeNumberRange(recognizeInput.getText(), recognizeInput.getCulture());
    }

    @Post(value = "/currency-range", produces = MediaType.APPLICATION_JSON)
    public List<ModelResult> recognizeCurrencyRange(@Body RecognizeInput recognizeInput) {
        String text = recognizeInput.getText();
        List<ModelResult> currencyModelResult = NumberWithUnitRecognizer.recognizeCurrency(recognizeInput.getText(), recognizeInput.getCulture());
        if (currencyModelResult == null || currencyModelResult.size() == 0) {
            // return empty list if no currency found
            return Lists.newArrayList();
        }

        int offset = 0;
        List<ModelResult> convertedCurrencyResult = new ArrayList<>();

        // replace the currency occurrences to numbers
        for (ModelResult result : currencyModelResult) {
            String replacement = result.resolution.get("value").toString();
            int newStart = offset + result.start;

            String newText = text.substring(0, offset + result.start) + replacement + ((offset + result.end + 1 < text.length()) ? text.substring(offset + result.end + 1) : "");
            offset += replacement.length() - text.substring(offset + result.start, offset + result.end + 1).length();

            int newEnd = offset + result.end;
            text = newText;

            SortedMap<String, Object> newResolution = new TreeMap<>(result.resolution);
            newResolution.put("original", result.text);
            newResolution.put("oldStart", result.start);
            newResolution.put("oldEnd", result.end);

            convertedCurrencyResult.add(new ModelResult(replacement, newStart, newEnd, result.typeName, newResolution));
        }

        List<ModelResult> rangeResult = NumberRecognizer.recognizeNumberRange(text, recognizeInput.getCulture());
        rangeResult.addAll(convertedCurrencyResult);

        return rangeResult;

//        if (modelResult != null && modelResult.size() > 0) {
//
//        }
//        return NumberRecognizer.recognizeNumberRange(recognizeInput.getText(), recognizeInput.getCulture());
    }
}
